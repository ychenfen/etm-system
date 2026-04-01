package com.etm.modules.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.etm.modules.teacher.dto.TeacherQueryDTO;
import com.etm.modules.teacher.entity.TeacherInfo;
import com.etm.modules.teacher.vo.TeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherInfoMapper extends BaseMapper<TeacherInfo> {

    IPage<TeacherInfo> selectTeacherPage(IPage<TeacherInfo> page, @Param("query") TeacherQueryDTO query);

    TeacherVO selectTeacherDetail(@Param("id") Long id);
}
