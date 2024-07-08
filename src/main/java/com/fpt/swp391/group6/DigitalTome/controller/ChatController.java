package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private MessageEntity receivePublicMessage(@Payload MessageEntity message) {
        return message;
    }

    @MessageMapping("/private-message")
    public MessageEntity receivePrivateMessage(@Payload MessageEntity message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver().getFullname(), "/private", message);
        return message;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 728ce2091d5a52ed77fa453748e001245b19c9ed
