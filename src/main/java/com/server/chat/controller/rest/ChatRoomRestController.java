package com.server.chat.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.chat.model.rest.common.DefRes;
import com.server.chat.service.ChatRoomService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/room")
public class ChatRoomRestController {

    private ChatRoomService chatRoomService;

    /**
     * @param name
     * @return HttpStatus
     * @desc 채팅방 생성 API
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestParam String name) {

        log.info("Create Chatting Room : {} ", name);

        if (this.chatRoomService.createByName(name)) {
            return ResponseEntity.ok(new DefRes(HttpStatus.OK));
        }

        return ResponseEntity.ok(new DefRes(HttpStatus.BAD_REQUEST));
    }

}
