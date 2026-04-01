package com.etm.modules.recruitment.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApprovalActionDTO;
import com.etm.modules.recruitment.dto.ApprovalCreateDTO;
import com.etm.modules.recruitment.dto.ApprovalQueryDTO;
import com.etm.modules.recruitment.entity.EmploymentApproval;
import com.etm.modules.recruitment.service.EmploymentApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 聘用审批Controller
 */
@Tag(name = "聘用审批管理")
@RestController
@RequestMapping("/employ/approval")
@RequiredArgsConstructor
public class EmploymentApprovalController {

    private final EmploymentApprovalService approvalService;

    @Operation(summary = "分页查询聘用审批")
    @GetMapping("/page")
    public Result<PageResult<EmploymentApproval>> getPage(ApprovalQueryDTO query) {
        PageResult<EmploymentApproval> result = approvalService.getApprovalPage(query);
        return Result.success(result);
    }

    @Operation(summary = "创建聘用审批")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<EmploymentApproval> create(@Valid @RequestBody ApprovalCreateDTO dto) {
        EmploymentApproval approval = approvalService.createApproval(dto);
        return Result.success(approval);
    }

    @Operation(summary = "执行审批操作")
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','HR','LEADER')")
    public Result<Void> approve(@PathVariable Long id, @Valid @RequestBody ApprovalActionDTO dto) {
        approvalService.doApprove(id, dto);
        return Result.success();
    }
}
