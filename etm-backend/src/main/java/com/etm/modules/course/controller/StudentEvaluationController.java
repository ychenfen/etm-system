package com.etm.modules.course.controller;

import com.etm.common.result.Result;
import com.etm.modules.course.dto.EvaluationCreateDTO;
import com.etm.modules.course.service.StudentEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学生评教Controller
 */
@Tag(name = "学生评教管理")
@RestController
@RequestMapping("/course/evaluation")
@RequiredArgsConstructor
public class StudentEvaluationController {

    private final StudentEvaluationService evaluationService;

    @Operation(summary = "提交评教")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','STUDENT')")
    public Result<Void> submitEvaluation(@Valid @RequestBody EvaluationCreateDTO dto) {
        evaluationService.submitEvaluation(dto);
        return Result.success();
    }

    @Operation(summary = "获取课程评教统计")
    @GetMapping("/statistics/{scheduleId}")
    public Result<Map<String, Object>> getStatistics(@PathVariable Long scheduleId) {
        Map<String, Object> statistics = evaluationService.getEvaluationStatistics(scheduleId);
        return Result.success(statistics);
    }
}
