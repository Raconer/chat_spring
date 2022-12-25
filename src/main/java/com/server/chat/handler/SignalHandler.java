package com.server.chat.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.chat.model.chat.Room;
import com.server.chat.model.chat.WebSocketMsg;
import com.server.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {

    private ChatRoomService chatRoomService;

    // message types, used in signalling:
    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "answer";
    // New ICE Candidate message
    private static final String MSG_TYPE_ICE = "ice";
    // join room data message
    private static final String MSG_TYPE_JOIN = "join";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[ws] Session has been closed with status [{} {}]", status, session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        /*
         * 웹 소켓이 연결되었을 때 클라이언트 쪽으로 메시지를 발송한다
         * 이때 원본 코드에서는 rooms.isEmpty() 가 false 를 전달한다. 이 의미는 현재 room 에 아무도 없다는 것을 의미하고
         * 따라서 추가적인 ICE 요청을 하지 않도록 한다.
         *
         * 현재 채팅 코드에서는 chatRoom 안에 userList 안에 user가 저장되기 때문에 rooms 이 아닌 userList 에 몇명이
         * 있는지 확인해야 했다.
         * 따라서 js 쪽에서 ajax 요청을 통해 rooms 가 아닌 userList 에 몇명이 있는지 확인하고
         * 2명 이상인 경우에만 JS에서 이와 관련된 변수를 true 가 되도록 변경하였다.
         *
         * 이렇게 true 상태가 되면 이후에 들어온 유저가 방안에 또 다른 유저가 있음을 확인하고,
         * P2P 연결을 시작한다.
         */
        sendMessage(session,
                new WebSocketMsg("Server", MSG_TYPE_JOIN, 12, null, null));

    }

    // 소켓 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Message Received");
        try {
            // 소켓쪽에서는 socket.send로 메시지를 발송한다. -> 참고로 JSON 형식으로 변환해서 전달해 온다.
            WebSocketMsg msg = objectMapper.readValue(message.getPayload(), WebSocketMsg.class);
            log.debug("[ws] Message of {} type from {} received", msg.getType(), msg.getFrom());
            String userId = msg.getFrom();
            Integer roomId = msg.getRoomId();

            Room room = this.chatRoomService.findById(roomId);

            switch (msg.getType()) {
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate = msg.getCandidate();
                    Object sdp = msg.getSdp();

                    log.debug("[ws] Signal: {}",
                            candidate != null
                                    ? candidate.toString().substring(0, 64)
                                    : sdp.toString().substring(0, 64));

                    break;
                case MSG_TYPE_JOIN:
                    log.debug("[ws] {} has joined Room: #{}", userId, roomId);
                default:
                    log.debug("[ws] Type of the received message {} is undefined!", msg.getType());
            }

        } catch (Exception e) {
            log.warn("An error occured: {}", e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMsg message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.debug("An error occured: {}", e.getMessage());
        }
    }
}
