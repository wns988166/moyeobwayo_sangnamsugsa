package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private Socket socket;
    private String matchedUserId;
    private String userId = "22120050"; // 현재 사용자 ID
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 소켓 초기화 및 연결
        socket = SocketManager.getInstance().getSocket();
        socket.connect();

        // 매칭된 사용자 ID 가져오기
        matchedUserId = getIntent().getStringExtra("matchedUserId");
        if (matchedUserId == null || matchedUserId.isEmpty()) {
            Toast.makeText(this, "매칭된 사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // RecyclerView 초기화 및 메시지 관리
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messages = new ArrayList<>();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this, messages);
        chatRecyclerView.setAdapter(chatAdapter);

        // 메시지 입력 및 전송 버튼
        EditText messageEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);

        // 채팅방 입장
        socket.emit("join_room", "chat_" + userId + "_" + matchedUserId, (Ack) args -> {
            boolean success = (boolean) args[0];
            runOnUiThread(() -> {
                if (!success) {
                    Toast.makeText(this, "채팅방 입장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });

        // 소켓 이벤트 등록
        try {
            socket.on("chat_message", onChatMessage);
        } catch (Exception e) {
            Log.e("ChatActivity", "Error during socket setup", e);
        }

        // 메시지 전송
        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "빈 메시지는 전송할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject messageData = new JSONObject();
                messageData.put("senderId", userId);
                messageData.put("recipientId", matchedUserId);
                messageData.put("message", message);

                // 메시지 전송
                socket.emit("chat_message", messageData, (Ack) args -> {
                    boolean success = (boolean) args[0];
                    runOnUiThread(() -> {
                        if (!success) {
                            Toast.makeText(this, "메시지 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            addMessage("나: " + message); // 메시지 전송 성공 시 추가
                        }
                    });
                });

                messageEditText.setText(""); // 입력창 초기화
            } catch (Exception e) {
                Log.e("ChatActivity", "Error sending message", e);
                Toast.makeText(this, "메시지 전송 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final Emitter.Listener onChatMessage = args -> runOnUiThread(() -> {
        try {
            JSONObject data = (JSONObject) args[0];
            String senderId = data.optString("senderId");
            String message = data.optString("message");

            if (senderId != null && !senderId.equals(userId)) {
                addMessage("상대: " + message);
            }
        } catch (Exception e) {
            Log.e("ChatActivity", "Error processing incoming message", e);
        }
    });

    private void addMessage(String message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.off("chat_message", onChatMessage);
            socket.disconnect(); // 소켓 연결 해제
        }
    }
}
