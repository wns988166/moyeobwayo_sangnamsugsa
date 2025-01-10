package com.example.myapplication.ProfileOption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileOption1Activity extends AppCompatActivity {
    private Button mE, mI, bS, bN, tT, tF, iJ, iP, btnNext, btnPrev, btnBack;
    private TextView selectedMbtiTextView;
    private int mValue = -1;
    private int bValue = -1;
    private int tValue = -1;
    private int iValue = -1;
    private List<Integer> profileList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_option1);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // 초기화 및 데이터 로드
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
        mE = findViewById(R.id.m_E);
        mI = findViewById(R.id.m_I);
        bS = findViewById(R.id.b_S);
        bN = findViewById(R.id.b_N);
        tT = findViewById(R.id.t_T);
        tF = findViewById(R.id.t_F);
        iJ = findViewById(R.id.i_J);
        iP = findViewById(R.id.i_P);
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        btnBack = findViewById(R.id.btn_back);
        selectedMbtiTextView = findViewById(R.id.selectedMbtiTextView);
    }

    private void setListeners() {
        mE.setOnClickListener(v -> {
            mValue = 1;
            updateButtonBackground(mE, mI);
            saveValue("mValue", mValue);
        });
        mI.setOnClickListener(v -> {
            mValue = 0;
            updateButtonBackground(mI, mE);
            saveValue("mValue", mValue);
        });
        bS.setOnClickListener(v -> {
            bValue = 1;
            updateButtonBackground(bS, bN);
            saveValue("bValue", bValue);
        });
        bN.setOnClickListener(v -> {
            bValue = 0;
            updateButtonBackground(bN, bS);
            saveValue("bValue", bValue);
        });
        tT.setOnClickListener(v -> {
            tValue = 1;
            updateButtonBackground(tT, tF);
            saveValue("tValue", tValue);
        });
        tF.setOnClickListener(v -> {
            tValue = 0;
            updateButtonBackground(tF, tT);
            saveValue("tValue", tValue);
        });
        iJ.setOnClickListener(v -> {
            iValue = 1;
            updateButtonBackground(iJ, iP);
            saveValue("iValue", iValue);
        });
        iP.setOnClickListener(v -> {
            iValue = 0;
            updateButtonBackground(iP, iJ);
            saveValue("iValue", iValue);
        });

        btnNext.setOnClickListener(v -> {
            // SharedPreferences에서 userId 가져오기
            String userId = sharedPreferences.getString("userId", null);

            // userId가 null인 경우 처리
            if (userId == null) {
                Toast.makeText(this, "유저 ID를 찾을 수 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                Log.e("ProfileOption1Activity", "userId is null");
                return;
            } else {
                Log.d("ProfileOption1Activity", "Loaded userId: " + userId);
            }

            Intent intent = new Intent(this, ProfileOption2Activity.class);

            // 현재 선택된 값을 profileList에 추가
            profileList.add(mValue);
            profileList.add(bValue);
            profileList.add(tValue);
            profileList.add(iValue);

            // profileList와 userId 전달
            intent.putIntegerArrayListExtra("profileList", new ArrayList<>(profileList));
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        });


        btnPrev.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
        mValue = sharedPreferences.getInt("mValue", -1);
        bValue = sharedPreferences.getInt("bValue", -1);
        tValue = sharedPreferences.getInt("tValue", -1);
        iValue = sharedPreferences.getInt("iValue", -1);
        updateButtonStates();
    }

    private void updateButtonStates() {
        if (mValue == 1) updateButtonBackground(mE, mI);
        else if (mValue == 0) updateButtonBackground(mI, mE);
        if (bValue == 1) updateButtonBackground(bS, bN);
        else if (bValue == 0) updateButtonBackground(bN, bS);
        if (tValue == 1) updateButtonBackground(tT, tF);
        else if (tValue == 0) updateButtonBackground(tF, tT);
        if (iValue == 1) updateButtonBackground(iJ, iP);
        else if (iValue == 0) updateButtonBackground(iP, iJ);
    }

    private void updateButtonBackground(Button selectedButton, Button unselectedButton) {
        selectedButton.setBackgroundResource(R.drawable.option_bar_left_selected);
        unselectedButton.setBackgroundResource(R.drawable.option_bar_right);
    }

    private boolean isValuesSelected() {
        return mValue != -1 && bValue != -1 && tValue != -1 && iValue != -1;
    }
}