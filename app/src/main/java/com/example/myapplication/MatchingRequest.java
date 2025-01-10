package com.example.myapplication;

// 매칭 요청 데이터
public class MatchingRequest {
    private String id;       // 'userId' -> 'id'
    private String matching;

    public MatchingRequest(String id, String matching) {  // 생성자 수정
        this.id = id;
        this.matching = matching;
    }

    public String getId() {  // getter 수정
        return id;
    }

    public void setId(String id) {  // setter 수정
        this.id = id;
    }

    public String getMatching() {
        return matching;
    }

    public void setMatching(String matching) {
        this.matching = matching;
    }
}
