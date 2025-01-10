import mariadb
import numpy as np
import tensorflow as tf
import threading
import time

# 종료 신호 설정
stop_event = threading.Event()

# MariaDB 연결 설정
def get_db_connection():
    try:
        conn = mariadb.connect(
            user="root",
            password="0000",
            host="localhost",
            port=3306,
            database="y_1"
        )
        print("Database connected successfully", flush=True)
        return conn
    except mariadb.Error as e:
        print(f"Error connecting to MariaDB: {e}", flush=True)
        return None

# 매칭 로직
def calculate_similarity_and_match():
    print("Fetching data from the matching table...", flush=True)
    conn = get_db_connection()
    if conn is None:
        print("Database connection failed. Skipping this cycle.", flush=True)
        return

    cursor = conn.cursor()

    # `matching` 테이블에서 사용자 데이터 가져오기
    try:
        cursor.execute("""
            SELECT userId, Embti, Jmbti, Smbti, Tmbti, cleanliness, coldSens, drinkFreq, 
                   firstLesson, grade, heatSens, inComm, noiseSens, shareNeeds, sleepHabit, 
                   sleepSche, smoke, upSche
            FROM matching
        """)
        rows = cursor.fetchall()
        print(f"Fetched {len(rows)} rows from the database.", flush=True)
    except Exception as e:
        print(f"Error fetching data: {e}", flush=True)
        conn.close()
        return

    conn.close()

    # 데이터 로드
    user_data = [list(row) for row in rows]
    user_ids = [row[0] for row in rows]
    features = np.array([row[1:] for row in rows], dtype=np.float32)

    print("Calculating similarity between users...", flush=True)
    # 유사도 계산 (유클리디안 거리)
    matched_pairs = []
    matched_set = set()
    for i in range(len(features)):
        if user_ids[i] in matched_set:
            continue
        min_distance = float('inf')
        best_match = None
        for j in range(len(features)):
            if i == j or user_ids[j] in matched_set:
                continue
            distance = tf.norm(features[i] - features[j]).numpy()
            if distance < min_distance:
                min_distance = distance
                best_match = user_ids[j]

        if best_match:
            matched_pairs.append((user_ids[i], best_match))
            matched_set.update([user_ids[i], best_match])
            print(f"Matched {user_ids[i]} with {best_match} (Distance: {min_distance:.4f})", flush=True)

    print(f"Total matched pairs: {len(matched_pairs)}", flush=True)

    # 매칭 결과 저장
    print("Saving matching results to the database...", flush=True)
    conn = get_db_connection()
    if conn is None:
        print("Database connection failed while saving results.", flush=True)
        return

    cursor = conn.cursor()
    try:
        for userId1, userId2 in matched_pairs:
            cursor.execute("""
                INSERT IGNORE INTO matches (userId1, userId2)
                VALUES (?, ?)
            """, (userId1, userId2))
        conn.commit()
        print("Matching results saved successfully.", flush=True)
    except Exception as e:
        print(f"Error saving matching results: {e}", flush=True)
    finally:
        conn.close()

# 주기적으로 매칭 실행
def schedule_matching():
    while not stop_event.is_set():
        print(f"Starting new matching cycle at {time.strftime('%Y-%m-%d %H:%M:%S')}", flush=True)
        calculate_similarity_and_match()
        print("Waiting for the next cycle...", flush=True)
        time.sleep(300)  # 5분 주기

# 백그라운드 매칭 스레드 시작
matching_thread = threading.Thread(target=schedule_matching, daemon=True)
matching_thread.start()

# 메인 스레드 유지
try:
    while True:
        time.sleep(1)  # 메인 스레드 실행 유지
except KeyboardInterrupt:
    print("Shutting down gracefully...", flush=True)
    stop_event.set()
    matching_thread.join()
    print("Matching thread terminated.", flush=True)
