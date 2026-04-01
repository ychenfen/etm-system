package com.etm.modules.assessment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 考核配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_config")
public class AssessmentConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 维度名称
     */
    private String dimensionName;

    /**
     * 维度代码
     */
    private String dimensionCode;

    /**
     * 权重（百分比）
     */
    private Integer weight;

    /**
     * 维度说明
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
}
