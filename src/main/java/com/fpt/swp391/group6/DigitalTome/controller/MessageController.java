package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.MessageDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import com.fpt.swp391.group6.DigitalTome.service.MessageService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private UserService userService;
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/send/message")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@RequestBody MessageDto message) {
        messageService.saveMessage(message);
        template.convertAndSendToUser(message.getReceiver(), "/topic/messages", message);
        return message;
    }

    @GetMapping("/chat")
    public String chat(Model model, Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        AccountEntity user = userService.getCurrentUser(principal, oAuth2User);
        String username = user!=null ? user.getUsername() : "";
        model.addAttribute("username", username);
        return "chat";
    }

    @GetMapping("/conversation/{username1}/{username2}")
    @ResponseBody
    public List<MessageEntity> getConversation(@PathVariable String username1, @PathVariable String username2) {
        AccountEntity user1 =  userService.findByUsername(username1);
        AccountEntity user2 =  userService.findByUsername(username2);
        return messageService.getConversation(user1, user2);
    }
}
