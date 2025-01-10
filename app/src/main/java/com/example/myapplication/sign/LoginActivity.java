package com.example.myapplication.sign;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.example.myapplication.Admin.AdminActivity;
import com.example.myapplication.LoginRequest;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox autoLoginCheckBox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI 요소 초기화
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        autoLoginCheckBox = findViewById(R.id.checkBox);
        loginButton = findViewById(R.id.loginButton);

        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(v -> {
            String id = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // admin 계정 확인 로직
            if (id.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "관리자로 로그인 성공!", Toast.LENGTH_SHORT).show();
                navigateToAdminActivity();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(id, password);
            String jsonData = "{\"username\":\"" + loginRequest.getUsername() + "\",\"password\":\"" + loginRequest.getPassword() + "\"}";

            Log.d("Login Request Data", "Request data: " + jsonData); // Login 요청 데이터 로그

            // HTTP 요청 전송
            String response = sendDataToServer(jsonData);
            if (response != null) {
                Log.d("Login Response Data", "Response: " + response); // Login 응답 데이터 로그
                handleLoginResponse(response, id); // 사용자 ID 추가
            } else {
                Toast.makeText(this, "로그인에 실패했습니다. 서버 오류.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginResponse(String response, String userId) {
        if (response != null && response.contains("\"success\":true")) {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();
            navigateToMainActivity(userId);
        } else {
            Toast.makeText(this, "로그인 실패. 서버 응답 확인 필요.", Toast.LENGTH_SHORT).show();
            Log.e("Login Error", "Unexpected response: " + response);
        }
    }

    private void navigateToMainActivity(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("id", userId); // userId 전달
        startActivity(intent);
        finish();
    }

    private void navigateToAdminActivity() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        intent.putExtra("id", "admin"); // 관리자 ID 전달
        startActivity(intent);
        finish();
    }

    private String sendDataToServer(String jsonData) {
        Log.d("Send Data", "Request data: " + jsonData); // HTTP 요청 전에 로그 추가
        final String[] serverResponse = {null};
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            String serverUrl = "http://61.39.251.73:5000/login"; // 로그인 엔드포인트 URL
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(15000); // 연결 타임아웃 15초
                conn.setReadTimeout(15000);    // 읽기 타임아웃 15초
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Connection", "close"); // Keep-Alive 비활성화

                // 요청 데이터 전송
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonData.getBytes());
                    os.flush();
                }

                int responseCode = conn.getResponseCode();
                Log.d("Server Response", "Response Code: " + responseCode);

                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        Gson gson = new Gson();
                        Map<String, Object> responseMap = gson.fromJson(br, Map.class);
                        if (responseMap.containsKey("success") && Boolean.TRUE.equals(responseMap.get("success"))) {
                            serverResponse[0] = "{\"success\":true}";
                        } else {
                            serverResponse[0] = "{\"success\":false}";
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error reading server response or parsing JSON: ", e);
                        serverResponse[0] = "{\"success\":false}";
                    }
                } else {
                    Log.e("Error", "Unexpected Response Code: " + responseCode);
                    serverResponse[0] = "{\"success\":false}";
                }
            } catch (Exception e) {
                Log.e("Error", "Exception occurred during connection: ", e);
                serverResponse[0] = "{\"success\":false}";
            }
        });
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("Error", "ExecutorService interrupted: ", e);
        }
        return serverResponse[0];
    }
}
