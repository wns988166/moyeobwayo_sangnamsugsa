package com.example.myapplication;

public class LoginRequest {
    private String username; // 기존 id를 username으로 변경
    private String password;

    // 생성자
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter 메서드
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { // 필요에 따라 Setter 추가
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { // 필요에 따라 Setter 추가
        this.password = password;
    }
}
