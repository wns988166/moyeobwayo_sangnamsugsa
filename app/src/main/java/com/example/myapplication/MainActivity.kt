package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.ProfileOption.ProfileOption1Activity

class MainActivity : AppCompatActivity() {
    private var roommateChatButton: Button? = null
    private var checkInButton: Button? = null
    private var suggestionButton: Button? = null
    private var noticeButton: Button? = null
    private var profileButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences에 userId 저장
        val userId = intent.getStringExtra("id")
        if (userId != null) {
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userId", userId)
            editor.apply()
        } else {
            Toast.makeText(this, "유저 ID를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        // 버튼 초기화
        roommateChatButton = findViewById(R.id.roommateChatButton)
        checkInButton = findViewById(R.id.checkInButton)
        suggestionButton = findViewById(R.id.suggestionButton)
        noticeButton = findViewById(R.id.noticeButton)
        profileButton = findViewById(R.id.profileButton)

        // 버튼 리스너 설정
        roommateChatButton?.setOnClickListener {
            Toast.makeText(this, "룸메이트 채팅 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show()
            // 여기에 룸메이트 채팅 관련 코드를 추가하세요
        }

        checkInButton?.setOnClickListener {
            Toast.makeText(this, "점호 체크인 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show()
            // 여기에 점호 체크인 관련 코드를 추가하세요
        }

        suggestionButton?.setOnClickListener {
            val intent = Intent(this@MainActivity, SuggestionActivity::class.java)
            startActivity(intent)
        }

        noticeButton?.setOnClickListener {
            Toast.makeText(this, "공지사항 확인 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show()
            // 여기에 공지사항 확인 관련 코드를 추가하세요
        }

        profileButton?.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", "default_id")

            if (userId == "default_id") {
                Toast.makeText(this, "유저 ID를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "유저 ID: $userId", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ProfileOption1Activity::class.java)
                intent.putExtra("id", userId)  // UserID 추가
                startActivity(intent)
            }
        }
    }

}
