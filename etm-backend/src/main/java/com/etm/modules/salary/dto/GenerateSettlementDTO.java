package com.etm.modules.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 生成薪酬结算DTO
 */
@Data
public class GenerateSettlementDTO {

    /**
     * 结算周期 (YYYYMM)
     */
    @NotBlank(message = "结算周期不能为空")
    private String settlementPeriod;

    /**
     * 周期类型: 1-月 2-学期
     */
    @NotNull(message = "周期类型不能为空")
    private Integer periodType;

    /**
     * 学院ID
     */
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;
}
