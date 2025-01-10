package com.example.myapplication.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuggestionListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SuggestionAdapter adapter;
    private List<Suggestion> suggestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_list);

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 레이아웃 매니저 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter 연결 (예: 빈 리스트로 초기화)
        recyclerView.setAdapter(new SuggestionAdapter(new ArrayList<>()));
    }


    private void loadSuggestions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://yourserver.com") // 서버 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Suggestion>> call = apiService.getSuggestions();
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 서버에서 데이터 가져오기 성공
                    suggestionList = response.body();
                    adapter = new SuggestionAdapter(suggestionList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(SuggestionListActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Suggestion>> call, Throwable t) {
                Toast.makeText(SuggestionListActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
