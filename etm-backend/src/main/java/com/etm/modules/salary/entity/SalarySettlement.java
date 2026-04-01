package com.etm.modules.salary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 薪酬结算
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("salary_settlement")
public class SalarySettlement {

    @TableId(type = IdType.AUTO)
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
     * 结算周期 (YYYYMM)
     */
    private String settlementPeriod;

    /**
     * 周期类型: 1-月 2-学期
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
     * 核批状态: 0-待核 1-已核
     */
    private Integer verifyStatus;

    /**
     * 发放状态: 0-待发 1-已发
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
}
