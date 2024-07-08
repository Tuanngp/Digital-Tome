package com.fpt.swp391.group6.DigitalTome.dto.paymentResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentPageDTOResponse {
    private List<PaymentDTOResponse> payments;
    private int totalPages;
    private int currentPage;
}