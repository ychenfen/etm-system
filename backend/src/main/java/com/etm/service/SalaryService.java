package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Salary;

public interface SalaryService extends IService<Salary> {
    Page<Salary> pageList(int current, int size, Long teacherId, String month, String status);
    void approve(Long id);
    void pay(Long id);
}
