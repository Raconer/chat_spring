package com.server.chat.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.server.chat.model.chat.Room;
import com.server.chat.repository.chat.RoomRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private RoomRepository roomRepository;

    // CREATE
    // 채팅방 생성
    public boolean createByName(String name) {
        Room room = new Room();
        room.setName(name);
        room.setRegDate(new Date());
        return this.roomRepository.save(room) != null;
    }

    // READ
    public Room findById(Integer id) {
        return roomRepository.findById(id).orElse(null);
    }

    // 전체 채팅방 조회
    public List<Room> findAllRoom() {

        List<Room> chatRoomList = roomRepository.findAll();
        Collections.reverse(chatRoomList);
        return chatRoomList;
    }

}
