package com.fpt.swp391.group6.DigitalTome.utils;

import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentDTOResponse;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;

import java.util.List;

public class PaymentMapper {

    public static PaymentDTOResponse toPaymentDTO(PaymentEntity payment){
        return PaymentDTOResponse.builder()
                .id(payment.getId())
                .username(payment.getAccountEntity().getUsername())
                .price(payment.getDecimal())
                .bookTitle(payment.getBookEntity() != null ? payment.getBookEntity().getTitle() : "")
                .createdDate(payment.getCreatedDate())
                .success(payment.isSuccess())
                .build();
    }

    public static List<PaymentDTOResponse> toPaymentDTOList(List<PaymentEntity> payments) {
        return payments.stream()
                .map(PaymentMapper::toPaymentDTO)
                .toList();
    }
}
