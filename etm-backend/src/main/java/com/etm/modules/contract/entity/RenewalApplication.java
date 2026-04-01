package com.etm.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("renewal_application")
public class RenewalApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("contract_id")
    private Long contractId;

    @TableField("college_id")
    private Long collegeId;

    @TableField("renewal_start_date")
    private LocalDate renewalStartDate;

    @TableField("renewal_end_date")
    private LocalDate renewalEndDate;

    @TableField("renewal_reason")
    private String renewalReason;

    @TableField("course_plan")
    private String coursePlan;

    @TableField("proposed_salary")
    private BigDecimal proposedSalary;

    @TableField("approval_status")
    private Integer approvalStatus;

    @TableField("approver_id")
    private Long approverId;

    @TableField("approval_comment")
    private String approvalComment;

    @TableField("approval_time")
    private LocalDateTime approvalTime;

    @TableField("new_contract_id")
    private Long newContractId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
