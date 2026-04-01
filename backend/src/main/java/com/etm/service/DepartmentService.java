package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Department;
import java.util.List;

public interface DepartmentService extends IService<Department> {
    Page<Department> pageList(int current, int size, String keyword);
    List<Department> listAll();
}
