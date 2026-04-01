package com.etm.modules.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * 学生评教创建DTO
 */
@Data
public class EvaluationCreateDTO {

    /**
     * 课程排课ID
     */
    @NotNull(message = "课程排课ID不能为空")
    private Long scheduleId;

    /**
     * 清晰度评分 (1-5)
     */
    @NotNull(message = "清晰度评分不能为空")
    @Min(1)
    @Max(5)
    private Integer clarityScore;

    /**
     * 互动评分 (1-5)
     */
    @NotNull(message = "互动评分不能为空")
    @Min(1)
    @Max(5)
    private Integer interactionScore;

    /**
     * 作业评分 (1-5)
     */
    @NotNull(message = "作业评分不能为空")
    @Min(1)
    @Max(5)
    private Integer homeworkScore;

    /**
     * 态度评分 (1-5)
     */
    @NotNull(message = "态度评分不能为空")
    @Min(1)
    @Max(5)
    private Integer attitudeScore;

    /**
     * 总体评分 (1-5)
     */
    @NotNull(message = "总体评分不能为空")
    @Min(1)
    @Max(5)
    private Integer overallScore;

    /**
     * 评价意见
     */
    private String comments;

    /**
     * 是否匿名: 1-是 0-否
     */
    private Integer isAnonymous;
}
