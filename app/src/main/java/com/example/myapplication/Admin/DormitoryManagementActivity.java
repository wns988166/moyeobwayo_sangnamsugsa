package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DormitoryManagementActivity extends AppCompatActivity {

    private Button building1Button;
    private Button building2Button;
    private Button building3Button;
    private Button building4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_management);

        building1Button = findViewById(R.id.building1Button);
        building2Button = findViewById(R.id.building2Button);
        building3Button = findViewById(R.id.building3Button);
        building4Button = findViewById(R.id.building4Button);

        building1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFloorSelection("Building 1");
            }
        });

        building2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFloorSelection("Building 2");
            }
        });

        building3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFloorSelection("Building 3");
            }
        });

        building4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFloorSelection("Building 4");
            }
        });
    }

    private void navigateToFloorSelection(String building) {
        Intent intent = new Intent(this, FloorSelectionActivity.class);
        intent.putExtra("Building", building); // 관 정보 전달
        startActivity(intent);
    }
}