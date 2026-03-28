package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Teacher;
import java.util.List;

public interface TeacherService extends IService<Teacher> {
    Page<Teacher> pageList(int current, int size, String keyword, Long departmentId, String hireStatus);
    List<Teacher> listAll();
    void approve(Long id);
    void reject(Long id);
}
