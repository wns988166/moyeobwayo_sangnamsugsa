from flask import Flask, request, jsonify
import mariadb
import sys

app = Flask(__name__)

# MariaDB 연결 설정
try:
    conn = mariadb.connect(
        user="root",
        password="0000",
        host="localhost",
        port=3306,
        database="y_1"
    )
except mariadb.Error as e:
    print(f"Error connecting to MariaDB: {e}")
    sys.exit(1)

cur = conn.cursor()

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({"success": False, "message": "Username and password are required"}), 400

    try:
        cur.execute("SELECT username, role FROM users WHERE username=? AND password=?", (username, password))
        user = cur.fetchone()

        if user:
            return jsonify({
                "success": True,
                "message": "Login successful",
                "role": user[1]  # 'role'은 두 번째 컬럼입니다
            }), 200
        elif admin:
            return jsonify({
                "success": True,
                "message": "Login successful",
                "role": admin[2]  # 'role'은 두 번째 컬럼입니다
            }), 200
        else:
            return jsonify({"success": False, "message": "Invalid username or password"}), 401

    except mariadb.Error as e:
        print(f"Error: {e}")
        return jsonify({"success": False, "message": "An error occurred"}), 500

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)