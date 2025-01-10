package com.example.myapplication;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

public class SocketManager {
    private static volatile SocketManager instance; // 싱글톤 인스턴스
    private static Socket socket;

    private SocketManager() {
        // private 생성자
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }

    public synchronized void initializeSocket(String userId) {
        if (socket == null || !socket.connected()) { // 연결 상태 확인
            try {
                socket = IO.socket("http://61.39.251.73:220");
                socket.connect();
                socket.emit("user_connected", userId);
                System.out.println("Socket connected with userId: " + userId); // 로그 추가
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize socket: " + e.getMessage()); // 에러 처리
            }
        } else {
            System.out.println("Socket is already connected."); // 로그 추가
        }
    }

    public Socket getSocket() {
        if (socket == null || !socket.connected()) {
            throw new IllegalStateException("Socket is not initialized or connected. Call initializeSocket() first.");
        }
        return socket;
    }

    public synchronized void disconnect() {
        if (socket != null && socket.connected()) { // 연결된 경우에만 해제
            socket.disconnect();
            System.out.println("Socket disconnected."); // 로그 추가
        }
        socket = null;
    }
}
