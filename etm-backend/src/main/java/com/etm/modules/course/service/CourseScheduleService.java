package com.etm.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.ScheduleCreateDTO;
import com.etm.modules.course.dto.ScheduleQueryDTO;
import com.etm.modules.course.entity.CourseSchedule;

import java.util.List;

/**
 * 课程排课Service接口
 */
public interface CourseScheduleService extends IService<CourseSchedule> {

    /**
     * 分页查询课程排课
     */
    PageResult<CourseSchedule> getSchedulePage(ScheduleQueryDTO query);

    /**
     * 获取教师的课程排课列表
     */
    List<CourseSchedule> getTeacherSchedules(Long teacherId, String semester);

    /**
     * 创建课程排课
     */
    CourseSchedule createSchedule(ScheduleCreateDTO dto);

    /**
     * 更新课程排课
     */
    CourseSchedule updateSchedule(Long id, ScheduleCreateDTO dto);

    /**
     * 删除课程排课
     */
    void deleteSchedule(Long id);
}
