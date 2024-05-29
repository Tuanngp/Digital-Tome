package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.dto.PaymentDto;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.PaymentMapper;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;


    @Transactional
    public PaymentEntity savePayment(PaymentDto paymentDto){
        boolean isPaymentSuccesfull = paymentDto.isSuccess();

        PaymentEntity paymentEntity = paymentMapper.toEntity(paymentDto);
        paymentEntity.setSuccess(isPaymentSuccesfull);
        return paymentRepository.save(paymentEntity);
    }

}
