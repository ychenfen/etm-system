package com.etm.modules.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TerminationCreateDTO {
    private Long teacherId;
    private String teacherName;
    private Long contractId;
    private Integer terminationType;
    private String terminationReason;
    private LocalDate terminationDate;
}
