package com.etm.modules.recruitment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 聘用审批创建DTO
 */
@Data
public class ApprovalCreateDTO {

    /**
     * 招聘申请ID
     */
    @NotNull(message = "招聘申请ID不能为空")
    private Long applicationId;

    /**
     * 教师ID
     */
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    /**
     * 学院ID
     */
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    /**
     * 拟定薪资
     */
    @NotNull(message = "拟定薪资不能为空")
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal proposedSalary;

    /**
     * 聘用开始日期
     */
    @NotNull(message = "聘用开始日期不能为空")
    private LocalDate employStartDate;

    /**
     * 聘用结束日期
     */
    @NotNull(message = "聘用结束日期不能为空")
    private LocalDate employEndDate;

    /**
     * 申报原因
     */
    private String applyReason;
}
