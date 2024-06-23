package com.fpt.swp391.group6.DigitalTome.dto.paymentResponse;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createdDate;
    boolean success;
}
