package com.etm.modules.course.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.SupervisionCreateDTO;
import com.etm.modules.course.entity.TeachingSupervision;
import com.etm.modules.course.service.TeachingSupervisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 教学督导Controller
 */
@Tag(name = "教学督导管理")
@RestController
@RequestMapping("/course/supervision")
@RequiredArgsConstructor
public class TeachingSupervisionController {

    private final TeachingSupervisionService supervisionService;

    @Operation(summary = "分页查询教学督导记录")
    @GetMapping("/page")
    public Result<PageResult<TeachingSupervision>> getPage(Long scheduleId, Integer pageNum, Integer pageSize) {
        PageResult<TeachingSupervision> result = supervisionService.getSupervisionPage(
                scheduleId, pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10
        );
        return Result.success(result);
    }

    @Operation(summary = "创建教学督导记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public Result<TeachingSupervision> create(@Valid @RequestBody SupervisionCreateDTO dto) {
        TeachingSupervision supervision = supervisionService.createSupervision(dto);
        return Result.success(supervision);
    }
}
