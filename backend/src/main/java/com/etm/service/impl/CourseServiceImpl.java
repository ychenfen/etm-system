package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Course;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.CourseMapper;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Page<Course> pageList(int current, int size, String keyword, Long departmentId, Long teacherId) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Course::getName, keyword).or().like(Course::getCode, keyword));
        }
        if (departmentId != null) {
            wrapper.eq(Course::getDepartmentId, departmentId);
        }
        if (teacherId != null) {
            wrapper.eq(Course::getTeacherId, teacherId);
        }
        wrapper.orderByDesc(Course::getCreateTime);
        Page<Course> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(course -> {
            if (course.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(course.getDepartmentId());
                if (dept != null) course.setDepartmentName(dept.getName());
            }
            if (course.getTeacherId() != null) {
                Teacher teacher = teacherMapper.selectById(course.getTeacherId());
                if (teacher != null) course.setTeacherName(teacher.getName());
            }
        });
        return page;
    }
}
