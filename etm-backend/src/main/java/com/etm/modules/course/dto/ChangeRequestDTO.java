package com.etm.modules.course.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 课程变更申请DTO
 */
@Data
public class ChangeRequestDTO {

    /**
     * 课程排课ID
     */
    @NotNull(message = "课程排课ID不能为空")
    private Long scheduleId;

    /**
     * 变更类型: 1-调课 2-停课
     */
    @NotNull(message = "变更类型不能为空")
    private Integer changeType;

    /**
     * 变更原因
     */
    @NotBlank(message = "变更原因不能为空")
    private String reason;

    /**
     * 原计划日期
     */
    @NotNull(message = "原计划日期不能为空")
    private LocalDate originalDate;

    /**
     * 原计划节次
     */
    private String originalPeriod;

    /**
     * 新计划日期
     */
    private LocalDate newDate;

    /**
     * 新计划节次
     */
    private String newPeriod;

    /**
     * 新上课地点
     */
    private String newLocation;
}
