package com.etm.modules.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etm.modules.teacher.entity.TeacherCertificate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherCertificateMapper extends BaseMapper<TeacherCertificate> {
    int deleteByTeacherId(@Param("teacherId") Long teacherId);
}
