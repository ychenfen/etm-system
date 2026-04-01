package com.etm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.annotation.RequireRole;
import com.etm.dto.Result;
import com.etm.entity.Attendance;
import com.etm.entity.Salary;
import com.etm.entity.Teacher;
import com.etm.mapper.AttendanceMapper;
import com.etm.mapper.SalaryMapper;
import com.etm.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private SalaryMapper salaryMapper;

    @GetMapping("/page")
    public Result<Page<Teacher>> page(@RequestParam(defaultValue = "1") int current,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) Long departmentId,
                                      @RequestParam(required = false) String hireStatus) {
        return Result.success(teacherService.pageList(current, size, keyword, departmentId, hireStatus));
    }

    @GetMapping("/list")
    public Result<List<Teacher>> list() {
        return Result.success(teacherService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Teacher> getById(@PathVariable Long id) {
        return Result.success(teacherService.getById(id));
    }

    @PostMapping
    public Result<?> add(@RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return Result.success("添加成功", null);
    }

    @PutMapping
    public Result<?> update(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        LambdaQueryWrapper<Attendance> aw = new LambdaQueryWrapper<>();
        aw.eq(Attendance::getTeacherId, id);
        if (attendanceMapper.selectCount(aw) > 0) {
            return Result.error("该教师存在考勤记录，无法删除");
        }
        LambdaQueryWrapper<Salary> sw = new LambdaQueryWrapper<>();
        sw.eq(Salary::getTeacherId, id);
        if (salaryMapper.selectCount(sw) > 0) {
            return Result.error("该教师存在薪酬记录，无法删除");
        }
        teacherService.removeById(id);
        return Result.success("删除成功", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PutMapping("/approve/{id}")
    public Result<?> approve(@PathVariable Long id) {
        teacherService.approve(id);
        return Result.success("审核通过", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PutMapping("/reject/{id}")
    public Result<?> reject(@PathVariable Long id) {
        teacherService.reject(id);
        return Result.success("已拒绝", null);
    }
}
