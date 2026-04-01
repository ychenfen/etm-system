package com.etm.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课程变更申请
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("schedule_change_request")
public class ScheduleChangeRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程排课ID
     */
    private Long scheduleId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 变更类型: 1-调课 2-停课
     */
    private Integer changeType;

    /**
     * 变更原因
     */
    private String reason;

    /**
     * 原计划日期
     */
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

    /**
     * 审批状态: 0-待 1-通过 2-驳回
     */
    private Integer approvalStatus;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
