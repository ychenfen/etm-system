package com.etm.modules.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 薪酬规则DTO
 */
@Data
public class SalaryRuleDTO {

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    /**
     * 课程类型
     */
    @NotBlank(message = "课程类型不能为空")
    private String courseType;

    /**
     * 教学级别
     */
    @NotBlank(message = "教学级别不能为空")
    private String teachingLevel;

    /**
     * 基础薪资
     */
    @NotNull(message = "基础薪资不能为空")
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal baseSalary;

    /**
     * 规则说明
     */
    private String description;
}
