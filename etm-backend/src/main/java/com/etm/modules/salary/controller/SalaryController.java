package com.etm.modules.salary.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.salary.entity.SalaryRule;
import com.etm.modules.salary.entity.SalarySettlement;
import com.etm.modules.salary.dto.SalaryRuleDTO;
import com.etm.modules.salary.dto.SalarySettlementQueryDTO;
import com.etm.modules.salary.dto.GenerateSettlementDTO;
import com.etm.modules.salary.dto.VerifyDTO;
import com.etm.modules.salary.service.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 薪酬结算Controller
 */
@Tag(name = "薪酬结算管理")
@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @Data
    public static class BatchPayRequest {
        private List<Long> ids;
    }

    @Operation(summary = "获取薪酬规则列表")
    @GetMapping("/rule/list")
    public Result<List<SalaryRule>> getRuleList() {
        List<SalaryRule> rules = salaryService.getRuleList();
        return Result.success(rules);
    }

    @Operation(summary = "创建薪酬规则")
    @PostMapping("/rule")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<SalaryRule> createRule(@Valid @RequestBody SalaryRuleDTO dto) {
        SalaryRule rule = salaryService.createRule(dto);
        return Result.success(rule);
    }

    @Operation(summary = "更新薪酬规则")
    @PutMapping("/rule/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<SalaryRule> updateRule(@PathVariable Long id, @Valid @RequestBody SalaryRuleDTO dto) {
        SalaryRule rule = salaryService.updateRule(id, dto);
        return Result.success(rule);
    }

    @Operation(summary = "删除薪酬规则")
    @DeleteMapping("/rule/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> deleteRule(@PathVariable Long id) {
        salaryService.deleteRule(id);
        return Result.success();
    }

    @Operation(summary = "分页查询薪酬结算")
    @GetMapping("/settlement/page")
    public Result<PageResult<SalarySettlement>> getSettlementPage(SalarySettlementQueryDTO query) {
        PageResult<SalarySettlement> result = salaryService.getSettlementPage(query);
        return Result.success(result);
    }

    @Operation(summary = "获取薪酬结算详情")
    @GetMapping("/settlement/{id}")
    public Result<SalarySettlement> getSettlementDetail(@PathVariable Long id) {
        SalarySettlement settlement = salaryService.getSettlementDetail(id);
        return Result.success(settlement);
    }

    @Operation(summary = "生成薪酬结算")
    @PostMapping("/settlement/generate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> generateSettlements(@Valid @RequestBody GenerateSettlementDTO dto) {
        salaryService.generateSettlements(dto);
        return Result.success();
    }

    @Operation(summary = "核批薪酬结算")
    @PutMapping("/settlement/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> verifySettlement(@PathVariable Long id, @RequestBody VerifyDTO dto) {
        salaryService.verifySettlement(id, dto);
        return Result.success();
    }

    @Operation(summary = "批量发放薪酬")
    @PutMapping("/settlement/batch-pay")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> batchPay(@RequestBody BatchPayRequest request) {
        salaryService.batchPay(request.getIds());
        return Result.success();
    }
}
