package com.etm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.annotation.RequireRole;
import com.etm.dto.Result;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.TeacherMapper;
import com.etm.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeacherMapper teacherMapper;

    @GetMapping("/page")
    public Result<Page<Department>> page(@RequestParam(defaultValue = "1") int current,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String keyword) {
        return Result.success(departmentService.pageList(current, size, keyword));
    }

    @GetMapping("/list")
    public Result<List<Department>> list() {
        return Result.success(departmentService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getById(id));
    }

    @RequireRole({"ADMIN"})
    @PostMapping
    public Result<?> add(@RequestBody Department department) {
        departmentService.save(department);
        return Result.success("添加成功", null);
    }

    @RequireRole({"ADMIN"})
    @PutMapping
    public Result<?> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return Result.success("更新成功", null);
    }

    @RequireRole({"ADMIN"})
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        LambdaQueryWrapper<Teacher> tw = new LambdaQueryWrapper<>();
        tw.eq(Teacher::getDepartmentId, id);
        if (teacherMapper.selectCount(tw) > 0) {
            return Result.error("该院系下存在教师，无法删除");
        }
        departmentService.removeById(id);
        return Result.success("删除成功", null);
    }
}
