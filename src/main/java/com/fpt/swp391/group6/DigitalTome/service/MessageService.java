package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.MessageDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.MessageMapper;
import com.fpt.swp391.group6.DigitalTome.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    @Autowired
    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper, UserService userService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userService = userService;
    }

    public void saveMessage(MessageDto messageDto) {
        MessageEntity messageEntity = messageMapper.toEntity(messageDto);
        messageEntity.setSender(userService.findByUsername(messageDto.getSender()));
        messageEntity.setReceiver(userService.findByUsername(messageDto.getReceiver()));
        if(!messageEntity.getSender().getUsername().equals(messageEntity.getReceiver().getUsername())) {
            messageRepository.save(messageEntity);
        }
    }

    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

    public void deleteAllByReceiver(AccountEntity receiver) {
        messageRepository.deleteAllByReceiver(receiver);
    }

    public List<MessageEntity> getConversation(AccountEntity sender, AccountEntity receiver) {
        return messageRepository.findAllBySenderAndReceiver(sender, receiver);
    }

    public List<AccountEntity> getChatUsers(AccountEntity currentUser) {
        List<MessageEntity> messages = messageRepository.findBySenderOrReceiver(currentUser, currentUser);
        Set<AccountEntity> users = new HashSet<>();
        for (MessageEntity message : messages) {
            if (message.getSender().equals(currentUser)) {
                users.add(message.getReceiver());
            } else if(message.getReceiver().equals(currentUser)) {
                users.add(message.getSender());
            }
        }
        return new ArrayList<>(users);
    }
}
