package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SuggestionActivity extends AppCompatActivity {
    private EditText suggestionEditText;
    private ImageView attachedImage;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        suggestionEditText = findViewById(R.id.suggestionEditText);
        Button attachImageButton = findViewById(R.id.attachImageButton);
        attachedImage = findViewById(R.id.attachedImage);
        Button submitButton = findViewById(R.id.submitButton);

        // 학번을 가져오는 로직 (예: SharedPreferences에서)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        studentId = prefs.getString("studentId", "Unknown");

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        attachedImage.setImageURI(uri);
                        attachedImage.setVisibility(View.VISIBLE);
                        attachedImage.setTag(uri.toString());
                    }
                }
        );

        attachImageButton.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        submitButton.setOnClickListener(v -> submitSuggestion());
    }

    private void submitSuggestion() {
        String content = suggestionEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showAlert("오류", "건의사항 내용을 입력해주세요.");
            return;
        }

        String imageUri = attachedImage.getVisibility() == View.VISIBLE ?
                attachedImage.getTag().toString() : "";
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Suggestions", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String savedSuggestions = sharedPreferences.getString("suggestionList", "");
            String newSuggestion = studentId + "," + content + "," + imageUri + "," + date + ";";

            editor.putString("suggestionList", savedSuggestions + newSuggestion);
            editor.apply();

            showAlert("알림", "건의사항이 접수되었습니다.", true);
        } catch (Exception e) {
            showAlert("오류", "건의사항 저장 중 오류가 발생했습니다.");
        }
    }

    private void showAlert(String title, String message) {
        showAlert(title, message, false);
    }

    private void showAlert(String title, String message, boolean finishActivity) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", (dialog, which) -> {
                    if (finishActivity) finish();
                })
                .setCancelable(false)
                .show();
    }
}
