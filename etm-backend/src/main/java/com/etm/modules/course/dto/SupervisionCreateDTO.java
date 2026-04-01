package com.etm.modules.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDate;

/**
 * 教学督导创建DTO
 */
@Data
public class SupervisionCreateDTO {

    /**
     * 课程排课ID
     */
    @NotNull(message = "课程排课ID不能为空")
    private Long scheduleId;

    /**
     * 督导日期
     */
    @NotNull(message = "督导日期不能为空")
    private LocalDate supervisionDate;

    /**
     * 是否按时: 1-是 0-否
     */
    private Integer isOnTime;

    /**
     * 内容一致性 (1-5)
     */
    @NotNull(message = "内容一致性不能为空")
    @Min(1)
    @Max(5)
    private Integer contentConsistency;

    /**
     * 教学方法 (1-5)
     */
    @NotNull(message = "教学方法不能为空")
    @Min(1)
    @Max(5)
    private Integer teachingMethod;

    /**
     * 课堂氛围 (1-5)
     */
    @NotNull(message = "课堂氛围不能为空")
    @Min(1)
    @Max(5)
    private Integer classroomAtmosphere;

    /**
     * 学生反馈 (1-5)
     */
    @NotNull(message = "学生反馈不能为空")
    @Min(1)
    @Max(5)
    private Integer studentFeedback;

    /**
     * 问题说明
     */
    private String problems;

    /**
     * 改进建议
     */
    private String suggestions;
}
