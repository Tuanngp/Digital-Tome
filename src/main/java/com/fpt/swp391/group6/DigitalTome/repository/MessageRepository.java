package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>{
    void deleteAllByReceiver(AccountEntity receiver);
    List<MessageEntity> findAllBySenderAndReceiver(AccountEntity sender, AccountEntity receiver);
}
