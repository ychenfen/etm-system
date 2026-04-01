package com.etm.modules.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewalCreateDTO {
    private Long teacherId;
    private Long contractId;
    private Long collegeId;
    private LocalDate renewalStartDate;
    private LocalDate renewalEndDate;
    private String renewalReason;
    private String coursePlan;
    private BigDecimal proposedSalary;
}
