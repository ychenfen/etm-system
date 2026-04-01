package com.etm.modules.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程排课创建/更新DTO
 */
@Data
public class ScheduleCreateDTO {

    /**
     * 教师ID
     */
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    /**
     * 学院ID
     */
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    /**
     * 学期
     */
    @NotBlank(message = "学期不能为空")
    private String semester;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    /**
     * 班级名称
     */
    @NotBlank(message = "班级名称不能为空")
    private String className;

    /**
     * 学生数
     */
    @NotNull(message = "学生数不能为空")
    private Integer studentCount;

    /**
     * 周开始
     */
    @NotNull(message = "周开始不能为空")
    private Integer weekStart;

    /**
     * 周结束
     */
    @NotNull(message = "周结束不能为空")
    private Integer weekEnd;

    /**
     * 星期几 (1-7)
     */
    @NotNull(message = "星期几不能为空")
    private Integer dayOfWeek;

    /**
     * 节次开始
     */
    @NotNull(message = "节次开始不能为空")
    private Integer periodStart;

    /**
     * 节次结束
     */
    @NotNull(message = "节次结束不能为空")
    private Integer periodEnd;

    /**
     * 上课地点
     */
    @NotBlank(message = "上课地点不能为空")
    private String location;

    /**
     * 总课时
     */
    @NotNull(message = "总课时不能为空")
    private BigDecimal totalHours;

    /**
     * 考试方式
     */
    private String examMethod;
}
