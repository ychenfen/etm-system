package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Page<Teacher> pageList(int current, int size, String keyword, Long departmentId, String hireStatus) {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Teacher::getName, keyword)
                    .or().like(Teacher::getPhone, keyword)
                    .or().like(Teacher::getWorkUnit, keyword));
        }
        if (departmentId != null) {
            wrapper.eq(Teacher::getDepartmentId, departmentId);
        }
        if (StringUtils.hasText(hireStatus)) {
            wrapper.eq(Teacher::getHireStatus, hireStatus);
        }
        wrapper.orderByDesc(Teacher::getCreateTime);
        Page<Teacher> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(teacher -> {
            if (teacher.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(teacher.getDepartmentId());
                if (dept != null) {
                    teacher.setDepartmentName(dept.getName());
                }
            }
        });
        return page;
    }

    @Override
    public List<Teacher> listAll() {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teacher::getHireStatus, "APPROVED");
        wrapper.orderByAsc(Teacher::getName);
        return this.list(wrapper);
    }

    @Override
    public void approve(Long id) {
        Teacher teacher = this.getById(id);
        if (teacher == null) throw new RuntimeException("教师不存在");
        if (!"PENDING".equals(teacher.getHireStatus())) {
            throw new RuntimeException("只有待审核状态的教师才能审核通过");
        }
        teacher.setHireStatus("APPROVED");
        this.updateById(teacher);
    }

    @Override
    public void reject(Long id) {
        Teacher teacher = this.getById(id);
        if (teacher == null) throw new RuntimeException("教师不存在");
        if (!"PENDING".equals(teacher.getHireStatus())) {
            throw new RuntimeException("只有待审核状态的教师才能拒绝");
        }
        teacher.setHireStatus("REJECTED");
        this.updateById(teacher);
    }
}
