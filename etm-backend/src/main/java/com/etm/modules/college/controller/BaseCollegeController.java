package com.etm.modules.college.controller;

import com.etm.common.result.PageResult;
import com.etm.common.result.Result;
import com.etm.modules.college.dto.CollegeDTO;
import com.etm.modules.college.dto.CollegeQueryDTO;
import com.etm.modules.college.entity.BaseCollege;
import com.etm.modules.college.service.BaseCollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "学院管理")
@RestController
@RequestMapping("/college")
@RequiredArgsConstructor
public class BaseCollegeController {

    private final BaseCollegeService collegeService;

    @Operation(summary = "分页查询学院")
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('admin','hr')")
    public Result<PageResult<BaseCollege>> page(CollegeQueryDTO query) {
        return Result.success(collegeService.getCollegePage(query));
    }

    @Operation(summary = "学院下拉列表")
    @GetMapping("/select")
    public Result<List<Map<String, Object>>> select() {
        return Result.success(collegeService.getCollegeSelect());
    }

    @Operation(summary = "获取学院详情")
    @GetMapping("/{id}")
    public Result<BaseCollege> getById(@PathVariable Long id) {
        return Result.success(collegeService.getById(id));
    }

    @Operation(summary = "新增学院")
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Result<BaseCollege> create(@Valid @RequestBody CollegeDTO dto) {
        return Result.success(collegeService.createCollege(dto));
    }

    @Operation(summary = "修改学院")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<BaseCollege> update(@PathVariable Long id, @Valid @RequestBody CollegeDTO dto) {
        return Result.success(collegeService.updateCollege(id, dto));
    }

    @Operation(summary = "删除学院")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> delete(@PathVariable Long id) {
        collegeService.deleteCollege(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "修改学院状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('admin')")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        collegeService.updateStatus(id, status);
        return Result.success("修改成功");
    }
}
