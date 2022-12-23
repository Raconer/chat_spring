package com.server.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.server.chat.dto.ChatDTO;
import com.server.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* 
 * - 채팅을 수신/송신(pub.sub)하기 위한 Controller 
 * - @MessageMapping : 이 어노테이션은 Stomp에서 들어오는 Message를 서버에서 발송("pub")한 메시지가 도착하는 엔드포인트이다.
 * 여기서 "/chat/enterUser" 로 되어 있지만 실제로는 "/pub/chat/enterUser"로 발송해야 @MessageMapping로 받아 메소드가 실행된다.
 * 
 * - convertAndSend() : 이 메소도는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
 * 이를 통해서 도착 지점 즉 sub되는 지점으로 인자를 들어온 객체를 Message객체로 변환해서 해당 도착지점을 Sub하고 있는 모든 사용자에게 메시지를 보내게 된다.
 * 
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {


    private final SimpMessageSendingOperations template;

    @Autowired
    ChatService chatService;

    /**
     * @param chatDTO
     * @param headerAccessor
     * @desc User 방에 입장ㅅ
     */
    @MessageMapping("/chat/enterUser") // /pub/chat/enterUser 와 같다.("/pub" 생략)
    public void enterUser(@Payload ChatDTO chatDTO, SimpMessageHeaderAccessor headerAccessor) {

        // 반환 결과를 Socket Session에 UserUUID로 저장
        headerAccessor.getSessionAttributes().put("userId", chatDTO.getUserName());
        headerAccessor.getSessionAttributes().put("roomId", chatDTO.getRoomId());
        headerAccessor.getSessionAttributes().put("userName", chatDTO.getUserName());

        chatDTO.setMessage(chatDTO.getUserName() + "님 입장!f!");
        template.convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);

    }

    /**
     * @param chatDTO
     * @deprecated 사용자 메시지 전송시
     */

    // * 메시지 Send => /pub/chat/message 로 요청
    // * 처리가 완료시 /sub/chat/room/roomId로 메시지가 전송
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chatDTO) {
        log.info("CHAT {}", chatDTO);
        chatDTO.setMessage(chatDTO.getMessage());
        template.convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);
    }

    /**
     * @param event
     * @desc 사용자 퇴장시
     */
    @Deprecated
    @EventListener
    public void webSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        // TODO 현재는 사용자 정보를 불러오지 않아 임시로 test로 처리 추후 퇴장한 사용자 정보를 받아 오는 처리가 필요하다.
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        if (userName != null) {
            ChatDTO chatDto = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEAVE)
                    .userName(userName)
                    .message(userName + "님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chatDto);
        }
    }
}
