package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 聘用审批表
 * 三级审批：学院负责人 → 人事处薪酬岗 → 人事处处长
 */
@Data
@TableName("approval")
public class Approval {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联外聘教师 ID */
    private Long teacherId;

    /** 审批编号，如 APR202401001 */
    private String approvalNo;

    /** 申请学院 ID */
    private Long departmentId;

    /** 拟聘开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /** 拟聘结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /** 拟定薪酬（元/课时） */
    private java.math.BigDecimal proposedSalary;

    /** 申请理由 */
    private String applyReason;

    /**
     * 当前审批节点：
     *   college_leader  - 学院负责人（第1关）
     *   hr_salary       - 人事处薪酬岗（第2关）
     *   hr_director     - 人事处处长（第3关）
     *   finished        - 全部完成
     */
    private String currentNode;

    /**
     * 总体审批状态：
     *   PENDING  - 审批中
     *   APPROVED - 已通过
     *   REJECTED - 已驳回
     *   REVOKED  - 已撤回
     */
    private String approvalStatus;

    /* ---- 第1节点：学院负责人 ---- */
    private String collegeStatus;
    private String collegeRemark;
    private String collegeBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collegeTime;

    /* ---- 第2节点：人事处薪酬岗 ---- */
    private String hrSalaryStatus;
    private String hrSalaryRemark;
    private String hrSalaryBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hrSalaryTime;

    /* ---- 第3节点：人事处处长 ---- */
    private String hrDirectorStatus;
    private String hrDirectorRemark;
    private String hrDirectorBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hrDirectorTime;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String teacherName;

    @TableField(exist = false)
    private String departmentName;
}
