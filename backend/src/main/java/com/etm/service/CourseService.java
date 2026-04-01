package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Course;

public interface CourseService extends IService<Course> {
    Page<Course> pageList(int current, int size, String keyword, Long departmentId, Long teacherId);
}
