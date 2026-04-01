package com.etm.modules.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etm.modules.course.entity.ScheduleChangeRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程变更申请Mapper
 */
@Mapper
public interface ScheduleChangeRequestMapper extends BaseMapper<ScheduleChangeRequest> {

}
