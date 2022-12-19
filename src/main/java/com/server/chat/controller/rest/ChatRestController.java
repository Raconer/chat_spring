package com.server.chat.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChatRestController {

    @RequestMapping("/mychat")
    public ModelAndView chat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chatting");
        return mv;
    }
}
