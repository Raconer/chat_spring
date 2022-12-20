package com.server.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // TODO : STOMP 접속 주소 url => /ws-stomp
        registry.addEndpoint("/ws-stomp").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // TODO : 메시지 구독 요청 url -> 메시지 수신
        registry.enableSimpleBroker("/sub");
        // TODO : 메시지 발행 -> 메시지 송신
        registry.setApplicationDestinationPrefixes("/pub");
    }
}