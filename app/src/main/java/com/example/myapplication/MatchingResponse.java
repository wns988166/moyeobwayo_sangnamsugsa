package com.example.myapplication;

// 매칭 응답 데이터
public class MatchingResponse {
    private boolean success; // 매칭 성공 여부
    private String matchedUserId; // 매칭된 사용자 ID
    private String room; // 채팅방 ID (서버 응답에 포함된 데이터)
    private String message; // 서버에서 반환하는 메시지 (예: 오류 메시지 또는 상태 메시지)

    // 매칭 성공 여부 반환
    public boolean isSuccess() {
        return success;
    }

    // 매칭된 사용자 ID 반환
    public String getMatchedUserId() {
        return matchedUserId != null ? matchedUserId : "Unknown";
    }

    // 채팅방 ID 반환
    public String getRoom() {
        return room;
    }

    // 서버 응답 메시지 반환
    public String getMessage() {
        return message;
    }

    // Setter 메서드 (필요 시 추가 가능)
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMatchedUserId(String matchedUserId) {
        this.matchedUserId = matchedUserId;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
