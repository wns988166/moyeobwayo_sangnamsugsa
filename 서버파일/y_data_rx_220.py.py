from flask import Flask, request, jsonify
import mariadb
import os
from datetime import datetime
import tensorflow as tf
import numpy as np
import random

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
def upload_data():
    content_type = request.headers.get('Content-Type')
    print(f"Received Content-Type: {content_type}")

    try:
        if content_type == 'application/json':
            data = request.json
        elif request.is_json:
            data = request.get_json()

        print(f"Received data: {data}")

        # 데이터베이스 연결
        conn = get_db_connection()
        if conn is None:
            return jsonify({"success": False, "message": "Database connection failed"}), 500



        cursor = conn.cursor()

        # userId 제외한 데이터 삽입
        userId = data['userId']
        fields = ', '.join(data.keys())
        values = ', '.join(str(v) for v in data.values())

        # 기존에 있는 사용자 데이터 업데이트
        cursor.execute(f"""
            INSERT INTO matching ({fields})
            VALUES ({values})
            ON DUPLICATE KEY UPDATE {', '.join([f"{key}={value}" for key, value in data.items() if key != 'userId'])}
        """)
        conn.commit()

        return jsonify({"success": True, "message": "Data received and saved successfully"}), 200

    except Exception as e:
        print(f"Error: {str(e)}")
        return jsonify({"success": False, "message": f"Error saving data: {str(e)}"}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=220)