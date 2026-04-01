package com.etm.modules.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审批操作DTO
 */
@Data
public class ApprovalActionDTO {

    /**
     * 审批节点: collegeLeader, hrSalary, hrDirector
     */
    @NotBlank(message = "审批节点不能为空")
    private String node;

    /**
     * 审批状态: 1-通过 2-驳回
     */
    @NotNull(message = "审批状态不能为空")
    private Integer status;

    /**
     * 审批意见
     */
    private String remark;
}
