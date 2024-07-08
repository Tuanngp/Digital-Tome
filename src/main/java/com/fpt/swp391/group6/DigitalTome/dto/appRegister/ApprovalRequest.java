package com.fpt.swp391.group6.DigitalTome.dto.appRegister;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    private Long id;
    private boolean action;  // Approve hoặc Not Approve
}
