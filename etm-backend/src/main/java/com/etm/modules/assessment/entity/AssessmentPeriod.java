package com.etm.modules.assessment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考核周期
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_period")
public class AssessmentPeriod {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 周期名称
     */
    private String periodName;

    /**
     * 周期类型: 1-学期 2-年度
     */
    private Integer periodType;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 状态: 0-未开始 1-进行中 2-已结束
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
