package com.server.chat.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    // ENTER/LEAVE 일때 -> 입장 퇴장 이벤트가 실행
    // TALK -> message를 해당 채팅방을 SUB 하고 있는 모든 클라이언트에게 전달
    public enum MessageType {
        ENTER, // 입장
        TALK, // 채팅
        LEAVE; // 퇴장
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 채팅 방 ID
    private String userName; // 채팅 입력자
    private String message; // 메시지
    private String regDate; // 채팅 발송 시간
}
