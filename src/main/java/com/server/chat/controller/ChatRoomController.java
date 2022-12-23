package com.server.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.server.chat.service.ChatService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* 채팅방 CRUD 관리 */
@Slf4j
@Controller
@AllArgsConstructor
public class ChatRoomController {

    private ChatService chatService;

    // 채팅 리스트 화면
    // "/chat"로 요청이 들어오면 전체 채팅룸 리스트를 불러온다.
    /*
     * @GetMapping
     * public String main(Model model) {
     * log.info("Start Main");
     * model.addAttribute("chatRoomList", this.chatService.findAllRoom());
     * return "index";
     * }
     */



    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId를 확인후 해당 roomId를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom으로 보낸다.
    @GetMapping("/room")
    public String detail(Model model, String roomId) {

        log.info("Get ChatRoom Detail");
        model.addAttribute("room", this.chatService.findRoomById(roomId));
        return "chatroom";
    }

}
