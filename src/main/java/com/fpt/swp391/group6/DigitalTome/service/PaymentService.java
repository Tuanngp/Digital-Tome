package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public void save(PaymentEntity payment) {
        paymentRepository.save(payment);
    }

    public boolean existsByAccountEntityAndBookEntity(AccountEntity user, BookEntity book) {
        return paymentRepository.existsByAccountEntityAndBookEntity(user, book);
    }

    public BigDecimal calculateTotalAmount() {
        return paymentRepository.sumPrice();
    }
}
