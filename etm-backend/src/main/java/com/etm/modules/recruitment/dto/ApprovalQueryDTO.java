package com.etm.modules.recruitment.dto;

import lombok.Data;

/**
 * 聘用审批查询DTO
 */
@Data
public class ApprovalQueryDTO {

    /**
     * 页码，默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10
     */
    private Integer pageSize = 10;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 最终状态: 0-进行中 1-完成 2-驳回
     */
    private Integer finalStatus;

    /**
     * 当前用户权限节点: collegeLeader, hrSalary, hrDirector
     */
    private String currentUserNode;
}
