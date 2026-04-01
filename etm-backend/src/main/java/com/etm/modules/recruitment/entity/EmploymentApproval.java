package com.etm.modules.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 聘用审批
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("employment_approval")
public class EmploymentApproval {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 招聘申请ID
     */
    private Long applicationId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 拟定薪资
     */
    private BigDecimal proposedSalary;

    /**
     * 聘用开始日期
     */
    private LocalDate employStartDate;

    /**
     * 聘用结束日期
     */
    private LocalDate employEndDate;

    /**
     * 申报原因
     */
    private String applyReason;

    /**
     * 学院领导审批状态: 0-待 1-通过 2-驳回
     */
    private Integer collegeLeaderStatus;

    /**
     * 学院领导审批意见
     */
    private String collegeLeaderComment;

    /**
     * 学院领导审批时间
     */
    private LocalDateTime collegeLeaderTime;

    /**
     * HR薪资审批状态: 0-待 1-通过 2-驳回
     */
    private Integer hrSalaryStatus;

    /**
     * HR薪资审批意见
     */
    private String hrSalaryComment;

    /**
     * HR薪资审批时间
     */
    private LocalDateTime hrSalaryTime;

    /**
     * HR主任审批状态: 0-待 1-通过 2-驳回
     */
    private Integer hrDirectorStatus;

    /**
     * HR主任审批意见
     */
    private String hrDirectorComment;

    /**
     * HR主任审批时间
     */
    private LocalDateTime hrDirectorTime;

    /**
     * 最终状态: 0-进行中 1-完成 2-驳回
     */
    private Integer finalStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
