package com.etm.modules.course.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.ChangeRequestDTO;
import com.etm.modules.course.entity.ScheduleChangeRequest;
import com.etm.modules.course.service.ScheduleChangeRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 课程变更申请Controller
 */
@Tag(name = "课程变更申请管理")
@RestController
@RequestMapping("/course/change-request")
@RequiredArgsConstructor
public class ScheduleChangeRequestController {

    private final ScheduleChangeRequestService changeRequestService;

    @Data
    public static class ApproveRequest {
        private Integer status;
        private String remark;
    }

    @Operation(summary = "分页查询课程变更申请")
    @GetMapping("/page")
    public Result<PageResult<ScheduleChangeRequest>> getPage(Long collegeId, Integer pageNum, Integer pageSize) {
        PageResult<ScheduleChangeRequest> result = changeRequestService.getChangeRequestPage(
                collegeId, pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10
        );
        return Result.success(result);
    }

    @Operation(summary = "创建课程变更申请")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','TEACHER')")
    public Result<ScheduleChangeRequest> create(@Valid @RequestBody ChangeRequestDTO dto) {
        ScheduleChangeRequest request = changeRequestService.createChangeRequest(dto);
        return Result.success(request);
    }

    @Operation(summary = "审批课程变更申请")
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','DEAN','LEADER')")
    public Result<Void> approve(@PathVariable Long id, @RequestBody ApproveRequest request) {
        changeRequestService.approveChangeRequest(id, request.getStatus(), request.getRemark());
        return Result.success();
    }
}
