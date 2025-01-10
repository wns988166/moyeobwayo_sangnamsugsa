package com.example.myapplication.ProfileOption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileOption3Activity extends AppCompatActivity {
    private Button btnPrev, btnBack, btnNext;
    private RadioGroup internalCommunicationRadioGroup, heatSensitiveRadioGroup;
    private RadioGroup coldSensitiveRadioGroup, drinkingFrequencyRadioGroup;

    private int inCommValue = -1;
    private int heatSensValue = -1;
    private int coldSensValue = -1;
    private int drinkFreqValue = -1;

    private List<Integer> profileList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_option3);

        sharedPreferences = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);

        initializeViews();
        setListeners();
        loadProfileList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedData();
    }

    private void initializeViews() {
        btnPrev = findViewById(R.id.btn_prev);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        internalCommunicationRadioGroup = findViewById(R.id.internal_communication_radio_group);
        heatSensitiveRadioGroup = findViewById(R.id.heat_sensitive_radio_group);
        coldSensitiveRadioGroup = findViewById(R.id.cold_sensitive_radio_group);
        drinkingFrequencyRadioGroup = findViewById(R.id.drinking_frequency_radio_group);
    }

    private void setListeners() {
        btnPrev.setOnClickListener(v -> {
            Intent prevIntent = new Intent(this, ProfileOption2Activity.class);
            prevIntent.putIntegerArrayListExtra("profileList", new ArrayList<>(profileList));
            startActivity(prevIntent);
            finish();
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnNext.setOnClickListener(v -> {
            if (isValuesSelected()) {
                // ProfileOption4Activity로 이동
                Intent intent = new Intent(this, ProfileOption4Activity.class);

                // userId 값을 확인하고 전달
                if (userId == null) {
                    userId = getIntent().getStringExtra("id"); // 이전 액티비티에서 전달받은 ID 확인
                }
                intent.putExtra("id", userId); // userId 전달

                // 현재 선택된 값을 profileList에 추가
                profileList.add(inCommValue); // 커뮤니케이션 값 추가
                profileList.add(heatSensValue); // 열 감도 값 추가
                profileList.add(coldSensValue); // 냉감도 값 추가
                profileList.add(drinkFreqValue); // 음주 빈도 값 추가

                // ProfileOption3에서 선택한 데이터를 ProfileOption4Activity로 전달
                intent.putIntegerArrayListExtra("profileList", new ArrayList<>(profileList));

                startActivity(intent); // ProfileOption4Activity 시작
                finish(); // 현재 액티비티 종료
            } else {
                // 선택지가 입력되지 않았을 때 사용자에게 알림
                Toast.makeText(this, "모든 선택지를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        internalCommunicationRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            inCommValue = (checkedId == R.id.internal_communication_yes) ? 1 : 0;
            saveValue("internalCommunicationValue", inCommValue);
        });

        heatSensitiveRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            heatSensValue = (checkedId == R.id.heat_sensitive_yes) ? 1 : 0;
            saveValue("heatSensitiveValue", heatSensValue);
        });

        coldSensitiveRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            coldSensValue = (checkedId == R.id.cold_sensitive_yes) ? 1 : 0;
            saveValue("coldSensitiveValue", coldSensValue);
        });

        drinkingFrequencyRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.frequency_zero) drinkFreqValue = 0;
            else if (checkedId == R.id.frequency_third) drinkFreqValue = 1;
            else drinkFreqValue = 2;
            saveValue("drinkingFrequencyValue", drinkFreqValue);
        });
    }

    private void loadProfileList() {
        Intent intent = getIntent();
        if (intent.hasExtra("profileList")) {
            profileList = intent.getIntegerArrayListExtra("profileList");
        }
    }

    private void saveValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void loadSavedData() {
        inCommValue = sharedPreferences.getInt("internalCommunicationValue", -1);
        heatSensValue = sharedPreferences.getInt("heatSensitiveValue", -1);
        coldSensValue = sharedPreferences.getInt("coldSensitiveValue", -1);
        drinkFreqValue = sharedPreferences.getInt("drinkingFrequencyValue", -1);
        updateRadioGroups();
    }

    private void updateRadioGroups() {
        if (inCommValue != -1) {
            internalCommunicationRadioGroup.check(inCommValue == 1 ? R.id.internal_communication_yes : R.id.internal_communication_no);
        }
        if (heatSensValue != -1) {
            heatSensitiveRadioGroup.check(heatSensValue == 1 ? R.id.heat_sensitive_yes : R.id.heat_sensitive_no);
        }
        if (coldSensValue != -1) {
            coldSensitiveRadioGroup.check(coldSensValue == 1 ? R.id.cold_sensitive_yes : R.id.cold_sensitive_no);
        }
        if (drinkFreqValue != -1) {
            if (drinkFreqValue == 0) drinkingFrequencyRadioGroup.check(R.id.frequency_zero);
            else if (drinkFreqValue == 1) drinkingFrequencyRadioGroup.check(R.id.frequency_third);
            else drinkingFrequencyRadioGroup.check(R.id.frequency_fifth);
        }
    }

    private boolean isValuesSelected() {
        return inCommValue != -1 && heatSensValue != -1 &&
                coldSensValue != -1 && drinkFreqValue != -1;
    }
}
