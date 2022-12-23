package com.server.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.chat.dto.ChatRoomDTO;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomDTO, Integer> {

}
