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

/* 
 * - 채팅을 수신/송신(pub.sub)하기 위한 Controller 
 * - @MessageMapping : 이 어노테이션은 Stomp에서 들어오는 Message를 서버에서 발송("pub")한 메시지가 도착하는 엔드포인트이다.
 * 여기서 "/chat/enterUser" 로 되어 있지만 실제로는 "/pub/chat/enterUser"로 발송해야 @MessageMapping로 받아 메소드가 실행된다.
 * 
 * - convertAndSend() : 이 메소도는 매개변수로 각각 메시지의 도착 지점과 객체를 넣어준다.
 * 이를 통해서 도착 지점 즉 sub되는 지점으로 인자를 들어온 객체를 Message객체로 변환해서 해당 도착지점을 Sub하고 있는 모든 사용자에게 메시지를 보내게 된다.
 * 
 */

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    @Autowired
    ChatService chatService;

    // MessageMapping을 통해 WebSocket으로 들어오는 메시지를 발신 처리 한다.
    // 이때 클라이언트에서는 /pub/chat/message() 로 요청하게 되고 이것을 controller가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.

    @MessageMapping("/chat/enterUser") // /pub/chat/enterUser 와 같다.("/pub" 생략)
    public void enterUser(@Payload ChatDTO chatDTO, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = chatDTO.getRoomId();
        // 채팅방 User + 1
        this.chatService.plusUserCnt(roomId);

        // 채팅방 User 추가 및 UserUUID 반환
        String userId = this.chatService.addUser(roomId, chatDTO.getUserName());

        // 반환 결과를 Socket Session에 UserUUID로 저장
        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("roomId", chatDTO.getRoomId());

        chatDTO.setMessage(chatDTO.getUserName() + "님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);

    }

    // 해당 User
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chatDTO) {
        template.convertAndSend("/sub/chat/room" + chatDTO.getRoomId(), chatDTO);
    }

    // User 퇴장 시에는 EventListener을 통해서 User 퇴장을 확인
    @EventListener
    public void webSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid와 roomId를 확인해서 채팅방 User 리스트와 room에서 해당 User를 삭제
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        // 채팅방 User -1
        this.chatService.minusUserCnt(roomId);

        // 채팅방 User리스트에서 UUID User 닉네임 조회 및 리스트 삭제
        String userName = this.chatService.getUserName(roomId, userId);
        this.chatService.delUser(roomId, userId);

        if (userName != null) {
            ChatDTO chatDto = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEAVE)
                    .userName(userName)
                    .message(userName + "님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/shat/room/" + roomId, chatDto);
        }

    }

    // 채팅에 참여한 User 리스트 반환
    @GetMapping("/chat/userList")
    @ResponseBody
    public List<String> userList(String roomId) {
        return this.chatService.getUserList(roomId);
    }

    // 채팅에 참여한 User닉네임 중복확인
    @GetMapping("/chat/duplicateName")
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("userName") String userName) {
        // User 이름 확인
        userName = this.chatService.isDuplicateName(roomId, userName);
        return userName;
    }
}
