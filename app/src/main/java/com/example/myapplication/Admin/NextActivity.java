package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class NextActivity extends AppCompatActivity {

    private EditText noteEditText;
    private Button saveButton;
    private TextView locationTextView;
    private String uniqueKey; // 관과 층 정보를 결합한 고유 키

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        locationTextView = findViewById(R.id.floorTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        // 이전 화면에서 전달받은 관과 층 정보
        String building = getIntent().getStringExtra("Building");
        String floor = getIntent().getStringExtra("Floor");

        // 관과 층 정보를 조합하여 고유 키 생성
        uniqueKey = building + "_" + floor;

        // 화면에 관과 층 정보 표시
        locationTextView.setText(building + " " + floor + " 메모장");

        // 앱 시작 시 저장된 메모 불러오기
        loadNote();

        // 저장 버튼 클릭 이벤트
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    // 메모 저장
    private void saveNote() {
        String noteContent = noteEditText.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(uniqueKey, noteContent); // 고유 키를 사용해 메모 저장
        editor.apply();
        Toast.makeText(this, "메모가 저장되었습니다!", Toast.LENGTH_SHORT).show();
    }

    // 메모 불러오기
    private void loadNote() {
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);
        String savedNote = sharedPreferences.getString(uniqueKey, ""); // 기본값은 빈 문자열
        noteEditText.setText(savedNote);
    }
}
