package com.fpt.swp391.group6.DigitalTome.dto.paymentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String status;
    private String message;
    private String approvalUrl;
}
