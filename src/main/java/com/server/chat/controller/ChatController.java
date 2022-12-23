package com.server.chat.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.server.chat.model.chat.Chat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* 
 * 채팅 관련 Controller
 * PUB : 게시자
 * SUB : 구독자
 */
// TODO : if (headerAccessor != null)는 SonarLint 경고로 작성한 코드이며 아직 경고가 사라지지 않아 추후 변경 예정
@Slf4j
@Controller
@AllArgsConstructor
public class ChatController {

    /**
     * SimpMessageSendingOperations -> convertAndSend()
     * 이 메소드는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
     * 이를 통해서 도착 지점으로 인자를 들어온 객체를 Message객체로 변환해서 해당 도착지점의 Sub하고 있는 모든 사용자에게 메시지를
     * 보내게 된다.
     */
    private SimpMessageSendingOperations template;
    public static final String SUB_PATH = "/sub/chat/room/";

    /**
     * @param Chat
     * @param headerAccessor
     * @desc User 방에 입장시 실행
     * @MessageMapping : 이 어노테이션은 Stomp에서 들어오는 Message를 서버에서 발송("pub")한 메시지가 도착하는
     *                 엔드포인트이다.
     *                 여기서 "/chat/enterUser" 로 되어 있지만 실제로는 "/pub/chat/enterUser"로
     *                 발송해야 @MessageMapping가 실행된다.
     */
    @MessageMapping("/chat/enterUser") // /pub/chat/enterUser 와 같다.("/pub" 생략)
    public void enterUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor) {

        if (headerAccessor != null) {
            // 반환 결과를 Socket Session에 UserUUID로 저장
            headerAccessor.getSessionAttributes().put("userId", chat.getUserName());
            headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
            headerAccessor.getSessionAttributes().put("userName", chat.getUserName());

            chat.setMessage(chat.getUserName() + "님 입장!!!");
            template.convertAndSend(SUB_PATH.concat(chat.getRoomId()), chat);
        }
    }

    /**
     * @param Chat
     * @desc 사용자 메시지 전송시 실행.
     *       * 실제로 '/pub/chat/message' 로 Message Send 요청
     *       * 처리가 완료시 '/sub/chat/room/roomId' 로 메시지가 전송 된다. //
     *       frontend(js\chat\index.js)에서
     *       subscribe("/sub/chat/room/" + roomId, onMessageReceived)로 설정 하였다.
     */
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload Chat chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend(SUB_PATH.concat(chat.getRoomId()), chat);
    }

    /**
     * @param event
     * @desc 사용자 퇴장시
     */
    @EventListener
    public void webSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor != null) {
            String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
            String userName = (String) headerAccessor.getSessionAttributes().get("userName");
            if (userName != null) {
                Chat chatDto = Chat.builder()
                        .type(Chat.MessageType.LEAVE)
                        .userName(userName)
                        .message(userName + "님 퇴장!!!")
                        .build();

                template.convertAndSend(SUB_PATH.concat(roomId), chatDto);
            }
        }
    }
}
