package com.etm.modules.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.teacher.dto.TeacherCreateDTO;
import com.etm.modules.teacher.dto.TeacherQueryDTO;
import com.etm.modules.teacher.entity.TeacherInfo;
import com.etm.modules.teacher.vo.TeacherVO;

public interface TeacherInfoService extends IService<TeacherInfo> {

    PageResult<TeacherInfo> getTeacherPage(TeacherQueryDTO query);

    TeacherVO getTeacherDetail(Long id);

    TeacherInfo createTeacher(TeacherCreateDTO dto);

    TeacherInfo updateTeacher(Long id, TeacherCreateDTO dto);

    void deleteTeacher(Long id);

    void updateStatus(Long id, Integer status);
}
