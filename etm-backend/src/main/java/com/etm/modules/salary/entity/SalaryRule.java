package com.etm.modules.salary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 薪酬规则
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("salary_rule")
public class SalaryRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 课程类型
     */
    private String courseType;

    /**
     * 教学级别
     */
    private String teachingLevel;

    /**
     * 基础薪资
     */
    private BigDecimal baseSalary;

    /**
     * 规则说明
     */
    private String description;

    /**
     * 是否生效: 1-是 0-否
     */
    private Integer isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
}
