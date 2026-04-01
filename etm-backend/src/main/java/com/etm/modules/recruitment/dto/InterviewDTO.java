package com.etm.modules.recruitment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 面试评估DTO
 */
@Data
public class InterviewDTO {

    /**
     * 教学理念评分 (1-5)
     */
    @NotNull(message = "教学理念评分不能为空")
    @Min(1)
    @Max(5)
    private Integer teachingConcept;

    /**
     * 专业能力评分 (1-5)
     */
    @NotNull(message = "专业能力评分不能为空")
    @Min(1)
    @Max(5)
    private Integer professionalAbility;

    /**
     * 表达能力评分 (1-5)
     */
    @NotNull(message = "表达能力评分不能为空")
    @Min(1)
    @Max(5)
    private Integer expressionAbility;

    /**
     * 实践经验评分 (1-5)
     */
    @NotNull(message = "实践经验评分不能为空")
    @Min(1)
    @Max(5)
    private Integer practicalExperience;

    /**
     * 整体印象评分 (1-5)
     */
    @NotNull(message = "整体印象评分不能为空")
    @Min(1)
    @Max(5)
    private Integer overallImpression;

    /**
     * 面试评价
     */
    private String evaluation;

    /**
     * 是否推荐: 1-推荐 0-不推荐
     */
    private Integer isRecommend;
}
