package com.etm.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.ChangeRequestDTO;
import com.etm.modules.course.entity.ScheduleChangeRequest;

/**
 * 课程变更申请Service接口
 */
public interface ScheduleChangeRequestService extends IService<ScheduleChangeRequest> {

    /**
     * 分页查询课程变更申请
     */
    PageResult<ScheduleChangeRequest> getChangeRequestPage(Long collegeId, Integer pageNum, Integer pageSize);

    /**
     * 创建课程变更申请
     */
    ScheduleChangeRequest createChangeRequest(ChangeRequestDTO dto);

    /**
     * 审批课程变更申请
     */
    void approveChangeRequest(Long id, Integer status, String remark);
}
