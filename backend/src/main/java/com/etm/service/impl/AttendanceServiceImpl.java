package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Attendance;
import com.etm.entity.Course;
import com.etm.entity.Teacher;
import com.etm.mapper.AttendanceMapper;
import com.etm.mapper.CourseMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<Attendance> pageList(int current, int size, Long teacherId, Long courseId, String status, String month) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq(Attendance::getTeacherId, teacherId);
        }
        if (courseId != null) {
            wrapper.eq(Attendance::getCourseId, courseId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Attendance::getStatus, status);
        }
        if (StringUtils.hasText(month)) {
            // H2 兼容写法：按年月前缀匹配
            wrapper.apply("CAST(attendance_date AS VARCHAR) LIKE CONCAT({0}, '%')", month);
        }
        wrapper.orderByDesc(Attendance::getAttendanceDate);
        Page<Attendance> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(att -> {
            if (att.getTeacherId() != null) {
                Teacher teacher = teacherMapper.selectById(att.getTeacherId());
                if (teacher != null) att.setTeacherName(teacher.getName());
            }
            if (att.getCourseId() != null) {
                Course course = courseMapper.selectById(att.getCourseId());
                if (course != null) att.setCourseName(course.getName());
            }
        });
        return page;
    }
}
