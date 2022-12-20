package com.server.chat.dto;

import java.util.HashMap;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

// STOMP을 통해 Pub/Sub을 사용하면 구독자 관리가 알아서 된다.
// 따라서 따로 세션 관리를 하는 코드를 작성할 필요가 없다.
// 메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현할 필요가 없다.
@Data
@NoArgsConstructor
public class ChatRoomDTO {
    private String id;
    private String name;
    private int userCnt;

    private HashMap<String, String> userList = new HashMap<>();

    public ChatRoomDTO(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.userCnt = 0;
    }

    public ChatRoomDTO create(String name) {
        return new ChatRoomDTO(name);
    }
}
