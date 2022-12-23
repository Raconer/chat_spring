package com.server.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.server.chat.service.ChatRoomService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class MainController {

    private ChatRoomService chatRoomService;

    @GetMapping
    public String main(Model model) {
        log.info("Start Main");

        model.addAttribute("chatRoomList", this.chatRoomService.findAllRoom());
        return "index";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable int id, Model model) {
        log.info("Start Main");
        return "chatRoom";
    }

}