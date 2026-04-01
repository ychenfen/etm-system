package com.etm.modules.salary.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 薪酬结算VO（包含详细信息）
 */
@Data
public class SalarySettlementVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 结算单号
     */
    private String settlementNo;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 结算周期
     */
    private String settlementPeriod;

    /**
     * 周期类型
     */
    private Integer periodType;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 实际课时
     */
    private BigDecimal actualHours;

    /**
     * 基础薪资
     */
    private BigDecimal baseSalary;

    /**
     * 职称系数
     */
    private BigDecimal titleCoefficient;

    /**
     * 考核系数
     */
    private BigDecimal assessCoefficient;

    /**
     * 基础金额
     */
    private BigDecimal baseAmount;

    /**
     * 职称津贴
     */
    private BigDecimal titleAllowance;

    /**
     * 考核奖金
     */
    private BigDecimal assessBonus;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 扣款原因
     */
    private String deductionReason;

    /**
     * 应发合计
     */
    private BigDecimal totalAmount;

    /**
     * 核批状态
     */
    private Integer verifyStatus;

    /**
     * 发放状态
     */
    private Integer paymentStatus;

    /**
     * 发放时间
     */
    private LocalDateTime paymentTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 课时明细列表
     */
    private List<Map<String, Object>> details;
}
