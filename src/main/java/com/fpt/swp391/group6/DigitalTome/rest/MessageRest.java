//package com.fpt.swp391.group6.DigitalTome.rest;
//
//import com.fpt.swp391.group6.DigitalTome.service.MessageService;
//import com.fpt.swp391.group6.DigitalTome.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/messages")
//public class MessageRest {
//    private final MessageService messageService;
//    private final UserService userService;
//
//    @Autowired
//    public MessageRest(MessageService messageService, UserService userService) {
//        this.messageService = messageService;
//        this.userService = userService;
//    }
//
//    @GetMapping("/{receiver}")
//    public String getMessages(@RequestParam String receiver) {
////        return messageService.getMessages(userService.getCurrentUser());
//    }
//}
