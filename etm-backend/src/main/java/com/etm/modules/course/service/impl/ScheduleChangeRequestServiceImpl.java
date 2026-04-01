package com.etm.modules.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.common.utils.SecurityUtil;
import com.etm.modules.course.dto.ChangeRequestDTO;
import com.etm.modules.course.entity.CourseSchedule;
import com.etm.modules.course.entity.ScheduleChangeRequest;
import com.etm.modules.course.mapper.CourseScheduleMapper;
import com.etm.modules.course.mapper.ScheduleChangeRequestMapper;
import com.etm.modules.course.service.ScheduleChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 课程变更申请Service实现
 */
@Service
@RequiredArgsConstructor
public class ScheduleChangeRequestServiceImpl extends ServiceImpl<ScheduleChangeRequestMapper, ScheduleChangeRequest> implements ScheduleChangeRequestService {

    private final CourseScheduleMapper courseScheduleMapper;

    @Override
    public PageResult<ScheduleChangeRequest> getChangeRequestPage(Long collegeId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<ScheduleChangeRequest> wrapper = new LambdaQueryWrapper<>();
        if (collegeId != null) {
            // 需要通过课程排课表的collegeId过滤
            wrapper.orderByDesc(ScheduleChangeRequest::getCreateTime);
        } else {
            wrapper.orderByDesc(ScheduleChangeRequest::getCreateTime);
        }

        IPage<ScheduleChangeRequest> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public ScheduleChangeRequest createChangeRequest(ChangeRequestDTO dto) {
        CourseSchedule schedule = courseScheduleMapper.selectById(dto.getScheduleId());
        if (schedule == null) {
            throw new BusinessException("课程排课不存在");
        }

        ScheduleChangeRequest request = new ScheduleChangeRequest();
        request.setScheduleId(dto.getScheduleId());
        request.setTeacherId(SecurityUtil.getCurrentUserId());
        request.setChangeType(dto.getChangeType());
        request.setReason(dto.getReason());
        request.setOriginalDate(dto.getOriginalDate());
        request.setOriginalPeriod(dto.getOriginalPeriod());
        request.setNewDate(dto.getNewDate());
        request.setNewPeriod(dto.getNewPeriod());
        request.setNewLocation(dto.getNewLocation());
        request.setApprovalStatus(0); // 待审批
        request.setCreateTime(LocalDateTime.now());
        this.save(request);
        return request;
    }

    @Override
    public void approveChangeRequest(Long id, Integer status, String remark) {
        ScheduleChangeRequest request = this.getById(id);
        if (request == null) {
            throw new BusinessException("课程变更申请不存在");
        }
        request.setApprovalStatus(status);
        request.setApproverId(SecurityUtil.getCurrentUserId());
        request.setApprovalTime(LocalDateTime.now());
        this.updateById(request);
    }
}
