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


