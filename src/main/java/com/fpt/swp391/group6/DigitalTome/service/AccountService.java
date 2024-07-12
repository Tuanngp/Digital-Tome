package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    public AccountEntity save(AccountEntity accountEntity) {
        return accountRepository.save(accountEntity);
    }
}
