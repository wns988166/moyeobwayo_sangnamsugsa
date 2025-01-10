package com.example.myapplication.api;

import com.example.myapplication.model.Suggestion;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api/suggestions") // API 엔드포인트
    Call<List<Suggestion>> getSuggestions();
}
