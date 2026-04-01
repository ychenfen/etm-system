package com.etm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.dto.Result;
import com.etm.entity.Course;
import com.etm.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/page")
    public Result<Page<Course>> page(@RequestParam(defaultValue = "1") int current,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) Long departmentId,
                                     @RequestParam(required = false) Long teacherId) {
        return Result.success(courseService.pageList(current, size, keyword, departmentId, teacherId));
    }

    @GetMapping("/list")
    public Result<List<Course>> list(@RequestParam(required = false) Long teacherId) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) wrapper.eq(Course::getTeacherId, teacherId);
        wrapper.orderByAsc(Course::getName);
        return Result.success(courseService.list(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @PostMapping
    public Result<?> add(@RequestBody Course course) {
        courseService.save(course);
        return Result.success("添加成功", null);
    }

    @PutMapping
    public Result<?> update(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success("删除成功", null);
    }
}
