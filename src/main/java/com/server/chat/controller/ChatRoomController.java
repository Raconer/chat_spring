package com.server.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.server.chat.dto.ChatRoomDTO;
import com.server.chat.service.ChatService;

import lombok.AllArgsConstructor;

/* 채팅방 CRUD 관리 */

@Controller
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatRoomController {

    private ChatService chatService;

    // 채팅 리스트 화면
    // "/chat"로 요청이 들어오면 전체 채팅룸 리스트를 불러온다.
    @GetMapping
    public String main(Model model) {
        model.addAttribute("chatRoomList", this.chatService.findAllRoom());
        return "index";
    }

    // 채팅방 생성
    @PostMapping
    public String create(@RequestParam String name, RedirectAttributes rttr) {
        ChatRoomDTO chatRoomDTO = this.chatService.createByName(name);
        rttr.addFlashAttribute("roomName", chatRoomDTO);

        return "redirect:/chat";
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId를 확인후 해당 roomId를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom으로 보낸다.
    @GetMapping("/room")
    public String detail(Model model, String roomId) {
        model.addAttribute("room", this.chatService.findRoomById(roomId));
        return "chatroom";
    }

}
