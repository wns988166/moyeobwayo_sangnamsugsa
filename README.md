
데이터베이스 코드

CREATE DATABASE y_1;

CREATE TABLE users ( id INT AUTO_INCREMENT PRIMARY KEY, -- 사용자 고유 ID username VARCHAR(50) NOT NULL UNIQUE, -- 사용자 아이디 password VARCHAR(255) NOT NULL, -- 평문 비밀번호 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );

CREATE TABLE y_1 (
    userId INT PRIMARY KEY,
    Embti INT,
    Jmbti INT,
    Smbti INT,
    Tmbti INT,
    cleanliness INT,
    coldSens INT,
    drinkFreq INT,
    firstLesson INT,
    grade INT,
    heatSens INT,
    inComm INT,
    noiseSens INT,
    shareNeeds INT,
    sleepHabit INT,
    sleepSche INT,
    smoke INT,
    upSche INT,
    matched BOOLEAN DEFAULT FALSE  -- 매칭 여부를 추적하는 열
);


CREATE TABLE matches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId1 VARCHAR(20) NOT NULL,
    userId2 VARCHAR(20) NOT NULL,
    matchedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(userId1, userId2)
);




-----데이터 베이스에서 매치된 정보를 클라이언트로 보내는 서버---------

from flask import Flask, request, jsonify
import mariadb

app = Flask(__name__)

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
        return conn
    except mariadb.Error as e:
        print(f"Error connecting to MariaDB: {e}")
        return None

@app.route('/', methods=['POST'])
def check_matching():
    try:
        # 요청 데이터
        data = request.json
        user_id = data.get('id')  # 클라이언트에서 'id'로 전송
        matching_status = data.get('matching')

        if not user_id or matching_status != "1":
            return jsonify({"success": False, "message": "Invalid request data"}), 400

        # 데이터베이스 연결
        conn = get_db_connection()
        if conn is None:
            return jsonify({"success": False, "message": "Database connection failed"}), 500

        cursor = conn.cursor(dictionary=True)

        # matches 테이블에서 매칭 정보 조회
        query = """
            SELECT userId1 AS userId1, userId2 AS userId2
            FROM matches
            WHERE userId1 = %s OR userId2 = %s
        """
        cursor.execute(query, (user_id, user_id))
        result = cursor.fetchone()

        # 디버깅용 로그 추가
        print(f"Database query result: {result}")

        # 매칭 결과 처리
        if result:
            matched_id = result['userId2'] if result['userId1'] == user_id else result['userId1']
            response = {
                "success": True,
                "message": "Match found",
                "matched_userId": matched_id
            }
        else:
            response = {
                "success": False,
                "message": "No match found"
            }

        return jsonify(response), 200

    except Exception as e:
        # 디버깅용 로그 추가
        print(f"Error: {str(e)}")
        return jsonify({"success": False, "message": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=330)




----------

