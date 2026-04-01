package com.etm.modules.recruitment.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApplicationQueryDTO;
import com.etm.modules.recruitment.dto.InterviewDTO;
import com.etm.modules.recruitment.dto.SecondReviewDTO;
import com.etm.modules.recruitment.entity.RecruitApplication;
import com.etm.modules.recruitment.service.RecruitApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 招聘申请Controller
 */
@Tag(name = "招聘申请管理")
@RestController
@RequestMapping("/recruit/application")
@RequiredArgsConstructor
public class RecruitApplicationController {

    private final RecruitApplicationService applicationService;

    @Data
    public static class CreateApplicationRequest {
        private Long noticeId;
        private Long teacherId;
        private String selfIntroduction;
    }

    @Operation(summary = "分页查询招聘申请")
    @GetMapping("/page")
    public Result<PageResult<RecruitApplication>> getPage(ApplicationQueryDTO query) {
        PageResult<RecruitApplication> result = applicationService.getApplicationPage(query);
        return Result.success(result);
    }

    @Operation(summary = "创建招聘申请")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','TEACHER')")
    public Result<RecruitApplication> create(@Valid @RequestBody CreateApplicationRequest request) {
        RecruitApplication application = applicationService.createApplication(
                request.getNoticeId(),
                request.getTeacherId(),
                request.getSelfIntroduction()
        );
        return Result.success(application);
    }

    @Operation(summary = "进行初审")
    @PostMapping("/{id}/first-review")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> doFirstReview(@PathVariable Long id) {
        applicationService.doFirstReview(id);
        return Result.success();
    }

    @Operation(summary = "进行复审")
    @PutMapping("/{id}/second-review")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> doSecondReview(@PathVariable Long id, @Valid @RequestBody SecondReviewDTO dto) {
        applicationService.doSecondReview(id, dto);
        return Result.success();
    }

    @Operation(summary = "提交面试评估")
    @PostMapping("/{id}/interview")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> submitInterview(@PathVariable Long id, @Valid @RequestBody InterviewDTO dto) {
        applicationService.submitInterview(id, dto);
        return Result.success();
    }
}
