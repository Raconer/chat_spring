package com.server.chat.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ServerEndpoint("/chat")
public class ChatService {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    /*
     * 1) onOpen 메서드
     * 클라이언트가 ServerEndpoint값인 "/chat" url로 서버에 접속하게 되면 onOpen 메서드가 실행되며,
     * 클라이언트 정보를 매개변수인 Session 객체를 통해 전달받습니다.
     * 이때 정적 필드인 clients에 해당 session이 존재하지 않으면 clients에 접속된 클라이언트를 추가합니다.
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("Open Session : {}", session.toString());
        if (clients.contains(session)) {
            log.warn("Already Connect Session", session);
        } else {
            clients.add(session);
            log.info("Session Ope : {}", session);
        }
    }

    /*
     * 2) onMessage 메서드
     * 클라이언트로부터 메시지가 전달되면 WebSocketChat 클래스의 onMessage메서드에 의해 clients에 있는 모든
     * session에 메시지를 전달합니다.
     */
    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        log.info("Receive Message : {}", msg);
        for (Session client : clients) {
            log.info("Send Data : {}", msg);
            client.getBasicRemote().sendText(msg);
        }
    }

    /*
     * 3) onClose 메서드
     * 클라이언트가 url을 바꾸거나 브라우저를 종료하면 자동으로 onClose() 메서드가 실행되며 해당 클라이언트 정보를 clients에서
     * 제거합니다.
     */
    @OnClose
    public void onClose(Session session) {
        log.info("Session Close : {}", session);
        clients.remove(session);
    }
}
