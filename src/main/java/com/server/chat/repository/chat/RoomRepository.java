package com.server.chat.repository.chat;

import org.springframework.stereotype.Repository;

import com.server.chat.model.chat.Room;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

}
