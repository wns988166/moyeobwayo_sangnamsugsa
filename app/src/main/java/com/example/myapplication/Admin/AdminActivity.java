package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AdminActivity extends AppCompatActivity {

    private TextView adminWelcomeTextView;
    private Button manageDormitoryButton;
    private Button checkSuggestionsButton;
    private Button manageAttendanceButton;
    private Button sendNoticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // UI 요소 초기화
        adminWelcomeTextView = findViewById(R.id.adminWelcomeTextView);
        manageDormitoryButton = findViewById(R.id.manageDormitoryButton);
        checkSuggestionsButton = findViewById(R.id.checkSuggestionsButton);
        manageAttendanceButton = findViewById(R.id.manageAttendanceButton);
        sendNoticeButton = findViewById(R.id.sendNoticeButton);

        // 버튼 클릭 리스너 설정
        manageDormitoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, DormitoryManagementActivity.class);
                startActivity(intent);
            }
        });

        checkSuggestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, SuggestionListActivity.class);
                startActivity(intent);
            }
        });


        manageAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "점호 관리 기능 구현 예정", Toast.LENGTH_SHORT).show();
                // TODO: 점호 관리 기능 구현
            }
        });

        sendNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "공지사항 발송 기능 구현 예정", Toast.LENGTH_SHORT).show();
                // TODO: 공지사항 발송 기능 구현
            }
        });
    }
}
