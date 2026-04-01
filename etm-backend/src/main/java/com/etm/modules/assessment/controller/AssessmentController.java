package com.etm.modules.assessment.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.assessment.entity.AssessmentConfig;
import com.etm.modules.assessment.entity.AssessmentPeriod;
import com.etm.modules.assessment.entity.AssessmentRecord;
import com.etm.modules.assessment.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 考核评估Controller
 */
@Tag(name = "考核评估管理")
@RestController
@RequestMapping("/assessment")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @Data
    public static class ConfigUpdateRequest {
        private Integer weight;
        private Integer isActive;
    }

    @Data
    public static class PeriodCreateRequest {
        private String periodName;
        private Integer periodType;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data
    public static class GenerateRecordsRequest {
        private Long periodId;
        private List<Long> teacherIds;
    }

    @Operation(summary = "获取考核配置列表")
    @GetMapping("/config/list")
    public Result<List<AssessmentConfig>> getConfigList() {
        List<AssessmentConfig> configs = assessmentService.getConfigList();
        return Result.success(configs);
    }

    @Operation(summary = "更新考核配置")
    @PutMapping("/config/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> updateConfig(@PathVariable Long id, @RequestBody ConfigUpdateRequest request) {
        assessmentService.updateConfig(id, request.getWeight(), request.getIsActive());
        return Result.success();
    }

    @Operation(summary = "获取考核周期列表")
    @GetMapping("/period/list")
    public Result<List<AssessmentPeriod>> getPeriodList() {
        List<AssessmentPeriod> periods = assessmentService.getPeriodList();
        return Result.success(periods);
    }

    @Operation(summary = "创建考核周期")
    @PostMapping("/period")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<AssessmentPeriod> createPeriod(@RequestBody PeriodCreateRequest request) {
        AssessmentPeriod period = assessmentService.createPeriod(
                request.getPeriodName(),
                request.getPeriodType(),
                request.getStartDate(),
                request.getEndDate()
        );
        return Result.success(period);
    }

    @Operation(summary = "分页查询考核记录")
    @GetMapping("/record/page")
    public Result<PageResult<AssessmentRecord>> getRecordPage(Long teacherId, Long periodId, Integer pageNum, Integer pageSize) {
        PageResult<AssessmentRecord> result = assessmentService.getRecordPage(
                teacherId, periodId, pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10
        );
        return Result.success(result);
    }

    @Operation(summary = "获取考核记录详情")
    @GetMapping("/record/{id}")
    public Result<AssessmentRecord> getRecordDetail(@PathVariable Long id) {
        AssessmentRecord record = assessmentService.getRecordDetail(id);
        return Result.success(record);
    }

    @Operation(summary = "生成考核记录")
    @PostMapping("/record/generate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> generateRecords(@RequestBody GenerateRecordsRequest request) {
        assessmentService.generateRecords(request.getPeriodId(), request.getTeacherIds());
        return Result.success();
    }
}
