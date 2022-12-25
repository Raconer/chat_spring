package com.server.chat.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// WebRTC 연결 시 사용되는 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMsg {
    private String from; // 보내는 유저 UUID
    private String type; // 메시지 타입
    private Integer roomId; // roomId
    private Object candidate; // 상태
    private Object sdp; // sdp 정보
}
