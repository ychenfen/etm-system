package com.etm.modules.assessment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 考核明细
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_detail")
public class AssessmentDetail {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考核记录ID
     */
    private Long recordId;

    /**
     * 维度代码
     */
    private String dimensionCode;

    /**
     * 维度名称
     */
    private String dimensionName;

    /**
     * 原始分数
     */
    private BigDecimal rawScore;

    /**
     * 权重（百分比）
     */
    private Integer weight;

    /**
     * 加权分数
     */
    private BigDecimal weightedScore;

    /**
     * 扣分原因
     */
    private String deductionReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
