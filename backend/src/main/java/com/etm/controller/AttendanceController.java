package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.annotation.RequireRole;
import com.etm.dto.Result;
import com.etm.entity.Attendance;
import com.etm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/page")
    public Result<Page<Attendance>> page(@RequestParam(defaultValue = "1") int current,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) Long teacherId,
                                         @RequestParam(required = false) Long courseId,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String month) {
        return Result.success(attendanceService.pageList(current, size, teacherId, courseId, status, month));
    }

    @GetMapping("/{id}")
    public Result<Attendance> getById(@PathVariable Long id) {
        return Result.success(attendanceService.getById(id));
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PostMapping
    public Result<?> add(@RequestBody Attendance attendance) {
        attendanceService.save(attendance);
        return Result.success("添加成功", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PutMapping
    public Result<?> update(@RequestBody Attendance attendance) {
        attendanceService.updateById(attendance);
        return Result.success("更新成功", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        attendanceService.removeById(id);
        return Result.success("删除成功", null);
    }
}
