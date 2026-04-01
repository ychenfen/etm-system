package com.etm.modules.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.ScheduleCreateDTO;
import com.etm.modules.course.dto.ScheduleQueryDTO;
import com.etm.modules.course.entity.CourseSchedule;
import com.etm.modules.course.mapper.CourseScheduleMapper;
import com.etm.modules.course.service.CourseScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程排课Service实现
 */
@Service
@RequiredArgsConstructor
public class CourseScheduleServiceImpl extends ServiceImpl<CourseScheduleMapper, CourseSchedule> implements CourseScheduleService {

    @Override
    public PageResult<CourseSchedule> getSchedulePage(ScheduleQueryDTO query) {
        LambdaQueryWrapper<CourseSchedule> wrapper = new LambdaQueryWrapper<>();
        if (query.getTeacherId() != null) {
            wrapper.eq(CourseSchedule::getTeacherId, query.getTeacherId());
        }
        if (query.getCollegeId() != null) {
            wrapper.eq(CourseSchedule::getCollegeId, query.getCollegeId());
        }
        if (query.getSemester() != null) {
            wrapper.eq(CourseSchedule::getSemester, query.getSemester());
        }
        if (query.getCourseName() != null) {
            wrapper.like(CourseSchedule::getCourseName, query.getCourseName());
        }
        wrapper.eq(CourseSchedule::getDeleted, 0)
                .orderByDesc(CourseSchedule::getCreateTime);

        IPage<CourseSchedule> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public List<CourseSchedule> getTeacherSchedules(Long teacherId, String semester) {
        LambdaQueryWrapper<CourseSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseSchedule::getTeacherId, teacherId);
        if (semester != null) {
            wrapper.eq(CourseSchedule::getSemester, semester);
        }
        wrapper.eq(CourseSchedule::getDeleted, 0);
        return this.list(wrapper);
    }

    @Override
    public CourseSchedule createSchedule(ScheduleCreateDTO dto) {
        CourseSchedule schedule = new CourseSchedule();
        schedule.setTeacherId(dto.getTeacherId());
        schedule.setCollegeId(dto.getCollegeId());
        schedule.setSemester(dto.getSemester());
        schedule.setCourseName(dto.getCourseName());
        schedule.setClassName(dto.getClassName());
        schedule.setStudentCount(dto.getStudentCount());
        schedule.setWeekStart(dto.getWeekStart());
        schedule.setWeekEnd(dto.getWeekEnd());
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setPeriodStart(dto.getPeriodStart());
        schedule.setPeriodEnd(dto.getPeriodEnd());
        schedule.setLocation(dto.getLocation());
        schedule.setTotalHours(dto.getTotalHours());
        schedule.setExamMethod(dto.getExamMethod());
        schedule.setStatus(0); // 正常
        schedule.setCreateTime(LocalDateTime.now());
        schedule.setDeleted(0);
        this.save(schedule);
        return schedule;
    }

    @Override
    public CourseSchedule updateSchedule(Long id, ScheduleCreateDTO dto) {
        CourseSchedule schedule = this.getById(id);
        if (schedule == null || schedule.getDeleted() == 1) {
            throw new BusinessException("课程排课不存在");
        }
        schedule.setTeacherId(dto.getTeacherId());
        schedule.setCollegeId(dto.getCollegeId());
        schedule.setSemester(dto.getSemester());
        schedule.setCourseName(dto.getCourseName());
        schedule.setClassName(dto.getClassName());
        schedule.setStudentCount(dto.getStudentCount());
        schedule.setWeekStart(dto.getWeekStart());
        schedule.setWeekEnd(dto.getWeekEnd());
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setPeriodStart(dto.getPeriodStart());
        schedule.setPeriodEnd(dto.getPeriodEnd());
        schedule.setLocation(dto.getLocation());
        schedule.setTotalHours(dto.getTotalHours());
        schedule.setExamMethod(dto.getExamMethod());
        schedule.setUpdateTime(LocalDateTime.now());
        this.updateById(schedule);
        return schedule;
    }

    @Override
    public void deleteSchedule(Long id) {
        CourseSchedule schedule = this.getById(id);
        if (schedule == null || schedule.getDeleted() == 1) {
            throw new BusinessException("课程排课不存在");
        }
        schedule.setDeleted(1);
        schedule.setUpdateTime(LocalDateTime.now());
        this.updateById(schedule);
    }
}
