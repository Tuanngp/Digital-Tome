package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.MessageDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.MessageMapper;
import com.fpt.swp391.group6.DigitalTome.service.MessageService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private UserService userService;
    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(SimpMessagingTemplate template, MessageService messageService, MessageMapper messageMapper) {
        this.template = template;
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @MessageMapping("/send/message")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@RequestBody MessageDto message) {
        messageService.saveMessage(message);
        template.convertAndSendToUser(message.getReceiver(), "/topic/messages", message);
        template.convertAndSendToUser(message.getReceiver(), "/topic/notifications", message);
        return message;
    }

    @GetMapping("/chat")
    public String chat(Model model, Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        AccountEntity user = userService.getCurrentUser(principal, oAuth2User);
        model.addAttribute("account", user);
        return "chat";
    }

    @GetMapping("/chat/users")
    @ResponseBody
    public List<AccountEntity> getChatUsers(Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        AccountEntity user = userService.getCurrentUser(principal, oAuth2User);
        return messageService.getChatUsers(user);
    }

    @GetMapping("/conversation/{username1}/{username2}")
    @ResponseBody
    public List<MessageDto> getConversation(@PathVariable String username1, @PathVariable String username2) {
        AccountEntity user1 =  userService.findByUsername(username1);
        AccountEntity user2 =  userService.findByUsername(username2);
        List<MessageEntity> messagesSent = messageService.getConversation(user1, user2);
        List<MessageEntity> messagesReceived = messageService.getConversation(user2, user1);
        messagesSent.addAll(messagesReceived);
        messagesSent.sort(Comparator.comparing(MessageEntity::getCreatedDate));
        return messageMapper.toDto(messagesSent);
    }

    @DeleteMapping("/message/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessageById(id);
    }


}
