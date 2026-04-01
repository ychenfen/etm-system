package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Course;
import com.etm.entity.Evaluation;
import com.etm.entity.Teacher;
import com.etm.mapper.CourseMapper;
import com.etm.mapper.EvaluationMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<Evaluation> pageList(int current, int size, Long teacherId, String semester) {
        LambdaQueryWrapper<Evaluation> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) wrapper.eq(Evaluation::getTeacherId, teacherId);
        if (StringUtils.hasText(semester)) wrapper.eq(Evaluation::getSemester, semester);
        wrapper.orderByDesc(Evaluation::getEvaluationDate);
        Page<Evaluation> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(eval -> {
            if (eval.getTeacherId() != null) {
                Teacher teacher = teacherMapper.selectById(eval.getTeacherId());
                if (teacher != null) eval.setTeacherName(teacher.getName());
            }
            if (eval.getCourseId() != null) {
                Course course = courseMapper.selectById(eval.getCourseId());
                if (course != null) eval.setCourseName(course.getName());
            }
        });
        return page;
    }
}
