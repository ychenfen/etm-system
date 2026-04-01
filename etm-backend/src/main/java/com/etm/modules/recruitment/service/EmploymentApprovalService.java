package com.etm.modules.recruitment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApprovalActionDTO;
import com.etm.modules.recruitment.dto.ApprovalCreateDTO;
import com.etm.modules.recruitment.dto.ApprovalQueryDTO;
import com.etm.modules.recruitment.entity.EmploymentApproval;

/**
 * 聘用审批Service接口
 */
public interface EmploymentApprovalService extends IService<EmploymentApproval> {

    /**
     * 分页查询聘用审批
     */
    PageResult<EmploymentApproval> getApprovalPage(ApprovalQueryDTO query);

    /**
     * 创建聘用审批
     */
    EmploymentApproval createApproval(ApprovalCreateDTO dto);

    /**
     * 审批操作，根据node更新对应字段
     */
    void doApprove(Long id, ApprovalActionDTO dto);
}
