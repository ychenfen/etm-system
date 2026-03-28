package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Page<Department> pageList(int current, int size, String keyword) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Department::getName, keyword).or().like(Department::getCode, keyword);
        }
        wrapper.orderByDesc(Department::getCreateTime);
        Page<Department> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(dept -> {
            LambdaQueryWrapper<Teacher> tw = new LambdaQueryWrapper<>();
            tw.eq(Teacher::getDepartmentId, dept.getId());
            dept.setTeacherCount(teacherMapper.selectCount(tw));
        });
        return page;
    }

    @Override
    public List<Department> listAll() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getStatus, 1);
        wrapper.orderByAsc(Department::getCode);
        return this.list(wrapper);
    }
}
