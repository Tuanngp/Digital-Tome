package com.fpt.swp391.group6.DigitalTome.dto.paymentResponse;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTOResponse {
    Long id;
    String username;
    BigDecimal price;
    String bookTitle;
    Date createdDate;
    boolean success;
}
