package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Attendance;

public interface AttendanceService extends IService<Attendance> {
    Page<Attendance> pageList(int current, int size, Long teacherId, Long courseId, String status, String month);
}
