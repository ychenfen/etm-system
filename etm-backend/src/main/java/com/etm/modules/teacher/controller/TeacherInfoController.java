package com.etm.modules.teacher.controller;

import com.etm.common.result.PageResult;
import com.etm.common.result.Result;
import com.etm.modules.teacher.dto.TeacherCreateDTO;
import com.etm.modules.teacher.dto.TeacherQueryDTO;
import com.etm.modules.teacher.entity.TeacherInfo;
import com.etm.modules.teacher.service.TeacherInfoService;
import com.etm.modules.teacher.vo.TeacherVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "外聘老师管理")
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherInfoController {

    private final TeacherInfoService teacherService;

    @Operation(summary = "分页查询外聘老师")
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('admin','hr')")
    public Result<PageResult<TeacherInfo>> page(TeacherQueryDTO query) {
        return Result.success(teacherService.getTeacherPage(query));
    }

    @Operation(summary = "获取老师详情")
    @GetMapping("/{id}")
    public Result<TeacherVO> getById(@PathVariable Long id) {
        return Result.success(teacherService.getTeacherDetail(id));
    }

    @Operation(summary = "新增外聘老师")
    @PostMapping
    @PreAuthorize("hasAnyRole('admin','hr')")
    public Result<TeacherInfo> create(@Valid @RequestBody TeacherCreateDTO dto) {
        return Result.success(teacherService.createTeacher(dto));
    }

    @Operation(summary = "修改外聘老师信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin','hr')")
    public Result<TeacherInfo> update(@PathVariable Long id, @Valid @RequestBody TeacherCreateDTO dto) {
        return Result.success(teacherService.updateTeacher(id, dto));
    }

    @Operation(summary = "删除外聘老师")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "修改老师聘用状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('admin','hr')")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        teacherService.updateStatus(id, status);
        return Result.success("修改成功");
    }
}
