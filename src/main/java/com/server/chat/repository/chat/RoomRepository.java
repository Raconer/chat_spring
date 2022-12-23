package com.server.chat.repository.chat;

import com.server.chat.model.chat.Room;

import org.springframework.data.jpa.repository.JpaRepository;

// TODO : @Repository 어노테이션이 없어도 동작이 가능하다
public interface RoomRepository extends JpaRepository<Room, Integer> {

}
