package com.etm.modules.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etm.modules.course.entity.CourseSchedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程排课Mapper
 */
@Mapper
public interface CourseScheduleMapper extends BaseMapper<CourseSchedule> {

}
