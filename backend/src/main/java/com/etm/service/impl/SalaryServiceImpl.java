package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Salary;
import com.etm.entity.Teacher;
import com.etm.mapper.SalaryMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Page<Salary> pageList(int current, int size, Long teacherId, String month, String status) {
        LambdaQueryWrapper<Salary> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) wrapper.eq(Salary::getTeacherId, teacherId);
        if (StringUtils.hasText(month)) wrapper.eq(Salary::getMonth, month);
        if (StringUtils.hasText(status)) wrapper.eq(Salary::getStatus, status);
        wrapper.orderByDesc(Salary::getMonth);
        Page<Salary> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(salary -> {
            if (salary.getTeacherId() != null) {
                Teacher teacher = teacherMapper.selectById(salary.getTeacherId());
                if (teacher != null) salary.setTeacherName(teacher.getName());
            }
        });
        return page;
    }

    @Override
    public void approve(Long id) {
        Salary salary = this.getById(id);
        if (salary == null) throw new RuntimeException("薪酬记录不存在");
        salary.setStatus("APPROVED");
        this.updateById(salary);
    }

    @Override
    public void pay(Long id) {
        Salary salary = this.getById(id);
        if (salary == null) throw new RuntimeException("薪酬记录不存在");
        salary.setStatus("PAID");
        salary.setPayDate(LocalDate.now());
        this.updateById(salary);
    }
}
