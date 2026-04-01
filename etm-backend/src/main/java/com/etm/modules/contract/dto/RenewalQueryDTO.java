package com.etm.modules.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewalQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long teacherId;
    private Long collegeId;
    private Integer approvalStatus;
}
