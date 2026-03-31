package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Approval;

public interface ApprovalService extends IService<Approval> {
    /** 发起审批 */
    Approval submit(Approval approval);
    /** 节点审批：node=college_leader/hr_salary/hr_director，pass=true通过 */
    void approve(Long id, String node, boolean pass, String remark, String operatorName);
    /** 撤回（仅 PENDING 且第1节点未审状态可撤） */
    void revoke(Long id);
    /** 分页查询 */
    Page<Approval> pageList(int current, int size, Long teacherId, String status);
}
