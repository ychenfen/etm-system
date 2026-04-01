package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Contract;

public interface ContractService extends IService<Contract> {
    /** 审批通过后自动生成合同 */
    Contract generateFromApproval(Long approvalId);
    /** 教师签署 */
    void teacherSign(Long id);
    /** 学校盖章（完成签署） */
    void schoolSign(Long id);
    /** 分页查询 */
    Page<Contract> pageList(int current, int size, Long teacherId, String status);
}
