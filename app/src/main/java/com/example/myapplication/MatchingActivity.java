package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MatchingActivity extends AppCompatActivity {
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        // SocketManager의 싱글톤 인스턴스를 통해 소켓 가져오기
        socket = SocketManager.getInstance().getSocket();
        socket.connect();

        // 매칭 결과 이벤트 수신
        socket.on("match", onMatch);
    }

    private final Emitter.Listener onMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(() -> {
                String matchedUserId = (String) args[0];
                Toast.makeText(MatchingActivity.this, "매칭 성공! 상대: " + matchedUserId, Toast.LENGTH_SHORT).show();

                // 채팅 화면으로 전환
                Intent intent = new Intent(MatchingActivity.this, ChatActivity.class);
                intent.putExtra("matchedUserId", matchedUserId);
                startActivity(intent);
                finish();
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.off("match", onMatch);
        socket.disconnect();
    }
}
