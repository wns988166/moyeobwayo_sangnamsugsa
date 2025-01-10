package com.example.myapplication.ProfileOption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.request.ProfileRequest;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.myapplication.MatchingRequest;
import com.example.myapplication.MatchingResponse;
import com.example.myapplication.ChatActivity;

public class ProfileOption4Activity extends AppCompatActivity {
    private static final String SERVER_URL_DATA = "http://61.39.251.73:220/";
    private static final String SERVER_URL_MATCH = "http://61.39.251.73:330/";

    private RadioGroup cleanlinessRadioGroup;
    private RadioGroup noiseSensitivityRadioGroup;
    private RadioGroup firstSleepScheduleRadioGroup;
    private RadioGroup secondSleepScheduleRadioGroup;
    private RadioGroup firstUpScheduleRadioGroup;
    private RadioGroup secondUpScheduleRadioGroup;
    private Button btnBack, btnFinish, btnPrev;

    private int cleanlinessValue = -1;
    private int noiseSensitivityValue = -1;
    private int sleepScheduleValue = -1;
    private int upScheduleValue = -1;

    private List<Integer> profileList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String userId = "default_id";
    private int grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_option4);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

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
        cleanlinessRadioGroup = findViewById(R.id.cleanliness_radio_group);
        noiseSensitivityRadioGroup = findViewById(R.id.noise_sensitivity_radio_group);
        firstSleepScheduleRadioGroup = findViewById(R.id.first_sleep_schedule_radio_group);
        secondSleepScheduleRadioGroup = findViewById(R.id.second_sleep_schedule_radio_group);
        firstUpScheduleRadioGroup = findViewById(R.id.first_up_schedule_radio_group);
        secondUpScheduleRadioGroup = findViewById(R.id.second_up_schedule_radio_group);
        btnBack = findViewById(R.id.btn_back);
        btnFinish = findViewById(R.id.btn_finish);
        btnPrev = findViewById(R.id.btn_prev);
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnPrev.setOnClickListener(v -> {
            Intent prevIntent = new Intent(this, ProfileOption3Activity.class);
            prevIntent.putIntegerArrayListExtra("profileList", new ArrayList<>(profileList));
            startActivity(prevIntent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            if (validateInputs()) {
                saveProfileData();
                sendDataToServer();

                // 매칭 요청 처리 후 채팅 화면으로 이동
                requestMatchingFromServer(userId);
            } else {
                showToast("모든 선택지를 입력해주세요.");
            }
        });

        cleanlinessRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            cleanlinessValue = (checkedId == R.id.cleanliness_sensitive) ? 0 :
                    (checkedId == R.id.cleanliness_normal) ? 1 : 2;
            saveValue("cleanlinessValue", cleanlinessValue);
        });

        noiseSensitivityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            noiseSensitivityValue = (checkedId == R.id.noise_sensitive) ? 0 :
                    (checkedId == R.id.noise_normal) ? 1 : 2;
            saveValue("noiseSensitivityValue", noiseSensitivityValue);
        });

        firstSleepScheduleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateSleepScheduleValue());
        secondSleepScheduleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateSleepScheduleValue());
        firstUpScheduleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateUpScheduleValue());
        secondUpScheduleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateUpScheduleValue());
    }

    private void loadProfileList() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("id");
        Log.d("ProfileOption4Activity", "Intent userId: " + userId);

        if (userId == null || userId.equals("default_id")) {
            userId = sharedPreferences.getString("userId", "default_id");
            Log.d("ProfileOption4Activity", "SharedPreferences userId: " + userId);
        }

        if (intent.hasExtra("profileList")) {
            profileList = intent.getIntegerArrayListExtra("profileList");
            Log.d("ProfileOption4Activity", "ProfileList: " + profileList.toString());
        }
        grade = intent.getIntExtra("grade", 0);
        Log.d("ProfileOption4Activity", "Grade: " + grade);
    }

    private void saveValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void loadSavedData() {
        cleanlinessValue = sharedPreferences.getInt("cleanlinessValue", -1);
        noiseSensitivityValue = sharedPreferences.getInt("noiseSensitivityValue", -1);
        sleepScheduleValue = sharedPreferences.getInt("sleepScheduleValue", -1);
        upScheduleValue = sharedPreferences.getInt("upScheduleValue", -1);
        updateRadioGroups();
    }

    private void updateRadioGroups() {
        cleanlinessRadioGroup.check(
                (cleanlinessValue == 0) ? R.id.cleanliness_sensitive :
                        (cleanlinessValue == 1) ? R.id.cleanliness_normal : R.id.cleanliness_not_sensitive);

        noiseSensitivityRadioGroup.check(
                (noiseSensitivityValue == 0) ? R.id.noise_sensitive :
                        (noiseSensitivityValue == 1) ? R.id.noise_normal : R.id.noise_not_sensitive);

        if (sleepScheduleValue == 0) firstSleepScheduleRadioGroup.check(R.id.sleep_early);
        else if (sleepScheduleValue == 1) firstSleepScheduleRadioGroup.check(R.id.sleep_ten);
        else if (sleepScheduleValue == 2) firstSleepScheduleRadioGroup.check(R.id.sleep_normal);
        else if (sleepScheduleValue == 3) secondSleepScheduleRadioGroup.check(R.id.sleep_late);
        else if (sleepScheduleValue == 4) secondSleepScheduleRadioGroup.check(R.id.sleep_very_late);

        if (upScheduleValue == 0) firstUpScheduleRadioGroup.check(R.id.up_early);
        else if (upScheduleValue == 1) firstUpScheduleRadioGroup.check(R.id.up_seven);
        else if (upScheduleValue == 2) firstUpScheduleRadioGroup.check(R.id.up_normal);
        else if (upScheduleValue == 3) secondUpScheduleRadioGroup.check(R.id.up_late);
        else if (upScheduleValue == 4) secondUpScheduleRadioGroup.check(R.id.up_very_late);
    }

    private boolean validateInputs() {
        return cleanlinessValue != -1 && noiseSensitivityValue != -1 &&
                sleepScheduleValue != -1 && upScheduleValue != -1;
    }

    private void saveProfileData() {
        profileList.add(cleanlinessValue);
        profileList.add(noiseSensitivityValue);
        profileList.add(sleepScheduleValue);
        profileList.add(upScheduleValue);
    }

    private void updateSleepScheduleValue() {
        int firstGroupId = firstSleepScheduleRadioGroup.getCheckedRadioButtonId();
        int secondGroupId = secondSleepScheduleRadioGroup.getCheckedRadioButtonId();

        if (firstGroupId == R.id.sleep_early) sleepScheduleValue = 0;
        else if (firstGroupId == R.id.sleep_ten) sleepScheduleValue = 1;
        else if (firstGroupId == R.id.sleep_normal) sleepScheduleValue = 2;
        else if (secondGroupId == R.id.sleep_late) sleepScheduleValue = 3;
        else if (secondGroupId == R.id.sleep_very_late) sleepScheduleValue = 4;
    }

    private void updateUpScheduleValue() {
        int firstGroupId = firstUpScheduleRadioGroup.getCheckedRadioButtonId();
        int secondGroupId = secondUpScheduleRadioGroup.getCheckedRadioButtonId();

        if (firstGroupId == R.id.up_early) upScheduleValue = 0;
        else if (firstGroupId == R.id.up_seven) upScheduleValue = 1;
        else if (firstGroupId == R.id.up_normal) upScheduleValue = 2;
        else if (secondGroupId == R.id.up_late) upScheduleValue = 3;
        else if (secondGroupId == R.id.up_very_late) upScheduleValue = 4;
    }

    private void sendDataToServer() {
        ProfileRequest profileRequest = new ProfileRequest(
                userId,
                profileList.get(0), // Embti
                profileList.get(1), // Smbti
                profileList.get(2), // Tmbti
                profileList.get(3), // Jmbti
                profileList.get(4), // firstLesson
                profileList.get(5), // smoke
                profileList.get(6), // sleepHabit
                profileList.get(7), // grade
                profileList.get(8), // shareNeeds
                profileList.get(9), // inComm
                profileList.get(10), // heatSens
                profileList.get(11), // coldSens
                profileList.get(12), // drinkFreq
                cleanlinessValue,    // cleanliness (option4)
                noiseSensitivityValue, // noiseSens (option4)
                sleepScheduleValue,  // sleepSche (option4)
                upScheduleValue      // upSche (option4)
        );

        String jsonData = new Gson().toJson(profileRequest);
        Log.d("ServerData", "Sending JSON: " + jsonData);

        sendJsonToServer(jsonData, SERVER_URL_DATA);
    }

    private void sendJsonToServer(String jsonData, String serverUrl) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonData.getBytes("UTF-8"));
                }

                handleResponse(conn);
            } catch (Exception e) {
                Log.e("Network Error", "Exception: ", e);
                showToast("서버 통신 오류: " + e.getMessage());
            }
        });
    }

    private void handleResponse(HttpURLConnection conn) {
        try {
            int responseCode = conn.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            if (responseCode == 200) {
                showToast("응답 성공: " + response.toString());
            } else {
                Log.e("Server Error", "Error Response: " + response.toString());
                showToast("서버 오류: " + response.toString());
            }
        } catch (Exception e) {
            Log.e("Response Error", "Exception: ", e);
            showToast("응답 처리 오류: " + e.getMessage());
        }
    }

    private void requestMatchingFromServer(String userId) {
        String requestData = new Gson().toJson(new MatchingRequest(userId, "1"));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                URL url = new URL(SERVER_URL_MATCH);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(requestData.getBytes("UTF-8"));
                }

                int responseCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                if (responseCode == 200) {
                    MatchingResponse matchingResponse = new Gson().fromJson(response.toString(), MatchingResponse.class);
                    if (matchingResponse.isSuccess()) {
                        Intent chatIntent = new Intent(this, ChatActivity.class);
                        chatIntent.putExtra("matchedUserId", matchingResponse.getMatchedUserId());
                        startActivity(chatIntent);
                        finish();
                    } else {
                        showToast("매칭된 사용자가 없습니다.");
                    }
                } else {
                    showToast("매칭 요청 실패: " + response.toString());
                }
            } catch (Exception e) {
                Log.e("Network Error", "Exception: ", e);
                showToast("서버 통신 오류: " + e.getMessage());
            }
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
}
