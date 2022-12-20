package com.server.chat.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.server.chat.dto.ChatRoomDTO;

@Repository
public class ChatService {

    private Map<String, ChatRoomDTO> chatRoomMap;
    Random random;

    @PostConstruct
    private void init() {
        this.chatRoomMap = new LinkedHashMap<>();
    }

    // CREATE

    // RoomName으로 채팅방 만들기
    public ChatRoomDTO createByName(String name) {
        ChatRoomDTO chatRoom = new ChatRoomDTO().create(name);
        this.chatRoomMap.put(chatRoom.getId(), chatRoom);
        return chatRoom;
    }
    // READ

    // 전체 채팅방 조회
    public List<ChatRoomDTO> findAllRoom() {
        List<ChatRoomDTO> chatRoomList = new ArrayList<>(this.chatRoomMap.values());
        Collections.reverse(chatRoomList);
        return chatRoomList;
    }

    // Room ID 기준으로 채팅방 찾기
    public ChatRoomDTO findRoomById(String id) {
        return this.chatRoomMap.get(id);
    }

    // UPDATE

    // 채팅방 인원 + 1
    public void plusUserCnt(String id) {
        ChatRoomDTO chatRoom = chatRoomMap.get(id);
        chatRoom.setUserCnt(chatRoom.getUserCnt() + 1);
    }

    // 채팅방 인원 - 1
    public void minusUserCnt(String id) {
        ChatRoomDTO chatRoom = chatRoomMap.get(id);
        chatRoom.setUserCnt(chatRoom.getUserCnt() - 1);
    }

    // 사용자

    // CREATE

    // 채팅방 User 리스트에 User 추가
    public String addUser(String roomId, String userName) {
        ChatRoomDTO chatRoom = this.chatRoomMap.get(roomId);
        String userId = UUID.randomUUID().toString();

        // 아이디 중복 확인 후 User List에 추가
        chatRoom.getUserList().put(userId, userName);
        return userId;
    }

    // READ
    public String isDuplicateName(String roomId, String userName) {
        ChatRoomDTO chatRoom = this.chatRoomMap.get(roomId);
        String tmp = userName;

        // 만약 UserName이 중복이라면 랜덤한 숫자를 붙임
        // 이때 랜덤한 숫자를 붙였을 때 getUserlist 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
        while (chatRoom.getUserList().containsValue(tmp)) {
            this.random = new SecureRandom();
            int ranNum = random.nextInt(100);
            tmp = userName + ranNum;
        }

        return tmp;
    }

    // DELETE
    // 채팅방 User 리스트 삭제
    public void delUser(String roomId, String userId) {
        ChatRoomDTO chatRoom = this.chatRoomMap.get(roomId);
        chatRoom.getUserList().remove(userId);
    }

    // 채팅방 userName 조회
    public String getUserName(String roomId, String userId) {
        ChatRoomDTO chatRoom = this.chatRoomMap.get(roomId);
        return chatRoom.getUserList().get(userId);
    }

    // 채팅방 전체 UserList 조회
    public List<String> getUserList(String roomId) {
        List<String> list = new ArrayList<>();
        ChatRoomDTO chatRoom = this.chatRoomMap.get(roomId);
        // HashMap을 For문을 돌린 후
        // value 값만 뽑아서 List에 저장 후 Return
        chatRoom.getUserList().forEach((key, value) -> {
            list.add(value);
        });

        return list;
    }
}
