package com.etm.modules.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.result.PageResult;
import com.etm.common.utils.SecurityUtil;
import com.etm.modules.course.dto.SupervisionCreateDTO;
import com.etm.modules.course.entity.TeachingSupervision;
import com.etm.modules.course.mapper.TeachingSupervisionMapper;
import com.etm.modules.course.service.TeachingSupervisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 教学督导Service实现
 */
@Service
@RequiredArgsConstructor
public class TeachingSupervisionServiceImpl extends ServiceImpl<TeachingSupervisionMapper, TeachingSupervision> implements TeachingSupervisionService {

    @Override
    public PageResult<TeachingSupervision> getSupervisionPage(Long scheduleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<TeachingSupervision> wrapper = new LambdaQueryWrapper<>();
        if (scheduleId != null) {
            wrapper.eq(TeachingSupervision::getScheduleId, scheduleId);
        }
        wrapper.orderByDesc(TeachingSupervision::getCreateTime);

        IPage<TeachingSupervision> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public TeachingSupervision createSupervision(SupervisionCreateDTO dto) {
        TeachingSupervision supervision = new TeachingSupervision();
        supervision.setScheduleId(dto.getScheduleId());
        supervision.setSupervisorId(SecurityUtil.getCurrentUserId());
        supervision.setSupervisionDate(dto.getSupervisionDate());
        supervision.setIsOnTime(dto.getIsOnTime());
        supervision.setContentConsistency(dto.getContentConsistency());
        supervision.setTeachingMethod(dto.getTeachingMethod());
        supervision.setClassroomAtmosphere(dto.getClassroomAtmosphere());
        supervision.setStudentFeedback(dto.getStudentFeedback());
        supervision.setProblems(dto.getProblems());
        supervision.setSuggestions(dto.getSuggestions());
        supervision.setCreateTime(LocalDateTime.now());
        this.save(supervision);
        return supervision;
    }
}
