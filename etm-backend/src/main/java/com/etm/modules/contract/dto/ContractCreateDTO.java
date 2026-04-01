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
public class ContractCreateDTO {
    private Long teacherId;
    private String teacherName;
    private Long collegeId;
    private String collegeName;
    private LocalDate employStartDate;
    private LocalDate employEndDate;
    private BigDecimal salary;
    private String contractNo;
}
