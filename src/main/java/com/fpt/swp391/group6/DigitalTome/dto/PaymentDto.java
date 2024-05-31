package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private BigDecimal price;
    private Long accountId; // ID của tài khoản
    private Long bookId; // ID của sách
    private Date createdDate;
    private Long createdBy;
    private boolean success; // Trạng thái của giao dịch: true nếu thành công, false nếu thất bại
    private Date modifiedDate;
    private Long modifiedBy;
    // Các constructors, getters và setters
}

