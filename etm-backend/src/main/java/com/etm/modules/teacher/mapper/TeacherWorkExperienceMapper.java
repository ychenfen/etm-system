package com.etm.modules.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etm.modules.teacher.entity.TeacherWorkExperience;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherWorkExperienceMapper extends BaseMapper<TeacherWorkExperience> {
    int deleteByTeacherId(@Param("teacherId") Long teacherId);
}
