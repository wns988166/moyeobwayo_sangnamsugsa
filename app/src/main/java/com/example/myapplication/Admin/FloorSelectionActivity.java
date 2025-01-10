package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class FloorSelectionActivity extends AppCompatActivity {

    private String building; // 이전 화면에서 전달받은 건물 정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_selection);

        // 이전 화면에서 건물 정보를 전달받음
        building = getIntent().getStringExtra("Building");

        // 1층 버튼
        Button floor1Button = findViewById(R.id.floor1Button);
        floor1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextScreen(building, "1층");
            }
        });

        // 2층 버튼
        Button floor2Button = findViewById(R.id.floor2Button);
        floor2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextScreen(building, "2층");
            }
        });

        // 3층 버튼
        Button floor3Button = findViewById(R.id.floor3Button);
        floor3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextScreen(building, "3층");
            }
        });
    }

    private void navigateToNextScreen(String building, String floor) {
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("Building", building); // 건물 정보 전달
        intent.putExtra("Floor", floor); // 층 정보 전달
        startActivity(intent);
    }
}
