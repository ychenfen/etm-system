package com.etm.modules.course.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.ScheduleCreateDTO;
import com.etm.modules.course.dto.ScheduleQueryDTO;
import com.etm.modules.course.entity.CourseSchedule;
import com.etm.modules.course.service.CourseScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程排课Controller
 */
@Tag(name = "课程排课管理")
@RestController
@RequestMapping("/course/schedule")
@RequiredArgsConstructor
public class CourseScheduleController {

    private final CourseScheduleService scheduleService;

    @Operation(summary = "分页查询课程排课")
    @GetMapping("/page")
    public Result<PageResult<CourseSchedule>> getPage(ScheduleQueryDTO query) {
        PageResult<CourseSchedule> result = scheduleService.getSchedulePage(query);
        return Result.success(result);
    }

    @Operation(summary = "获取教师课程排课列表")
    @GetMapping("/teacher/{teacherId}")
    public Result<List<CourseSchedule>> getTeacherSchedules(@PathVariable Long teacherId, String semester) {
        List<CourseSchedule> schedules = scheduleService.getTeacherSchedules(teacherId, semester);
        return Result.success(schedules);
    }

    @Operation(summary = "获取课程排课详情")
    @GetMapping("/{id}")
    public Result<CourseSchedule> getById(@PathVariable Long id) {
        CourseSchedule schedule = scheduleService.getById(id);
        return Result.success(schedule);
    }

    @Operation(summary = "创建课程排课")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')")
    public Result<CourseSchedule> create(@Valid @RequestBody ScheduleCreateDTO dto) {
        CourseSchedule schedule = scheduleService.createSchedule(dto);
        return Result.success(schedule);
    }

    @Operation(summary = "更新课程排课")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')")
    public Result<CourseSchedule> update(@PathVariable Long id, @Valid @RequestBody ScheduleCreateDTO dto) {
        CourseSchedule schedule = scheduleService.updateSchedule(id, dto);
        return Result.success(schedule);
    }

    @Operation(summary = "删除课程排课")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN')")
    public Result<Void> delete(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.success();
    }
}
