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

public class ProfileOption2Activity extends AppCompatActivity {
    private Button btnPrev, btnBack, btnNext;
    private RadioGroup smokingRadioGroup, firstLessonRadioGroup;
    private RadioGroup sleepingHabitRadioGroup, sharingDailyNeedsRadioGroup;
    private RadioGroup grade_radio_group;

    private int gradeValue = -1;

    private int smokingValue = -1;
    private int firstLessonValue = -1;
    private int sleepingHabitValue = -1;
    private int sharingDailyNeedsValue = -1;

    private List<Integer> profileList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_option2);

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
        smokingRadioGroup = findViewById(R.id.smoking_radio_group);
        firstLessonRadioGroup = findViewById(R.id.first_lesson_radio_group);
        sleepingHabitRadioGroup = findViewById(R.id.sleeping_habit_radio_group);
        sharingDailyNeedsRadioGroup = findViewById(R.id.sharing_daily_needs_radio_group);
        grade_radio_group = findViewById(R.id.grade_radio_group);
    }

    private void setListeners() {
        btnPrev.setOnClickListener(v -> {
            Intent prevIntent = new Intent(this, ProfileOption1Activity.class);
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
                Intent intent = new Intent(this, ProfileOption3Activity.class);
                intent.putExtra("id", getIntent().getStringExtra("id")); // userId 전달 추가

                // 현재 선택된 값을 profileList에 추가
                profileList.add(gradeValue);
                profileList.add(smokingValue);
                profileList.add(firstLessonValue);
                profileList.add(sleepingHabitValue);
                profileList.add(sharingDailyNeedsValue);

                // 다음 액티비티로 데이터 전달
                intent.putIntegerArrayListExtra("profileList", new ArrayList<>(profileList));
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "모든 선택지를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        grade_radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.grade_1) gradeValue = 1;
            else if (checkedId == R.id.grade_2) gradeValue = 2;
            else if (checkedId == R.id.grade_3) gradeValue = 3;
            else if (checkedId == R.id.grade_4) gradeValue = 4;
            saveValue("gradeValue", gradeValue);
        });

        smokingRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            smokingValue = (checkedId == R.id.smoking_yes) ? 1 : 0;
            saveValue("smokingValue", smokingValue);
        });

        firstLessonRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            firstLessonValue = (checkedId == R.id.firstlesson_yes) ? 1 : 0;
            saveValue("firstLessonValue", firstLessonValue);
        });

        sleepingHabitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            sleepingHabitValue = (checkedId == R.id.sleepinghabit_yes) ? 1 : 0;
            saveValue("sleepingHabitValue", sleepingHabitValue);
        });

        sharingDailyNeedsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            sharingDailyNeedsValue = (checkedId == R.id.sharing_yes) ? 1 : 0;
            saveValue("sharingDailyNeedsValue", sharingDailyNeedsValue);
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
        smokingValue = sharedPreferences.getInt("smokingValue", -1);
        firstLessonValue = sharedPreferences.getInt("firstLessonValue", -1);
        sleepingHabitValue = sharedPreferences.getInt("sleepingHabitValue", -1);
        sharingDailyNeedsValue = sharedPreferences.getInt("sharingDailyNeedsValue", -1);
        gradeValue = sharedPreferences.getInt("gradeValue", -1);
        updateRadioGroups();
    }

    private void updateRadioGroups() {
        if (smokingValue != -1) {
            smokingRadioGroup.check(smokingValue == 1 ? R.id.smoking_yes : R.id.smoking_no);
        }
        if (firstLessonValue != -1) {
            firstLessonRadioGroup.check(firstLessonValue == 1 ? R.id.firstlesson_yes : R.id.firstlesson_no);
        }
        if (sleepingHabitValue != -1) {
            sleepingHabitRadioGroup.check(sleepingHabitValue == 1 ? R.id.sleepinghabit_yes : R.id.sleepinghabit_no);
        }
        if (sharingDailyNeedsValue != -1) {
            sharingDailyNeedsRadioGroup.check(sharingDailyNeedsValue == 1 ? R.id.sharing_yes : R.id.sharing_no);
        }
        if (gradeValue != -1) {
            if (gradeValue == 1) grade_radio_group.check(R.id.grade_1);
            else if (gradeValue == 2) grade_radio_group.check(R.id.grade_2);
            else if (gradeValue == 3) grade_radio_group.check(R.id.grade_3);
            else if (gradeValue == 4) grade_radio_group.check(R.id.grade_4);
        }

    }

    private boolean isValuesSelected() {
        return smokingValue != -1 && firstLessonValue != -1 &&
                sleepingHabitValue != -1 && sharingDailyNeedsValue != -1 && gradeValue != -1;

    }
}
