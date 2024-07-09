package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.MessageDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.MessageMapper;
import com.fpt.swp391.group6.DigitalTome.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        messages.sort(Comparator.comparing(MessageEntity::getCreatedDate).reversed());
        Set<AccountEntity> users = new LinkedHashSet<>();
        // get all users that have conversation with current user
        for (MessageEntity message : messages) {
            if (message.getSender().equals(currentUser)) {
                users.add(message.getReceiver());
            } else if(message.getReceiver().equals(currentUser)) {
                users.add(message.getSender());
            }
        }
        return new ArrayList<>(users);
    }

    public void setMessageToRead(MessageDto message) {
        AccountEntity sender = userService.findByUsername(message.getSender());
        AccountEntity receiver = userService.findByUsername(message.getReceiver());
        List<MessageEntity> messages = messageRepository.findAllBySenderAndReceiver(receiver, sender);
        messages.forEach(messageToRead -> {
            messageToRead.setStatus("read");
            messageRepository.save(messageToRead);
        });
    }

    public int getUnreadCount(AccountEntity user) {
        return messageRepository.countByReceiverAndStatus(user, "unread");
    }

    public List<MessageEntity> getLatestMessages(AccountEntity user) {
        return messageRepository.findTop5ByReceiverOrderByCreatedDateDesc(user);
    }
}
