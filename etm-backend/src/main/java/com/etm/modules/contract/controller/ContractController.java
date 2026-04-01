package com.etm.modules.contract.controller;

import com.etm.common.result.PageResult;
import com.etm.common.result.Result;
import com.etm.modules.contract.dto.ContractCreateDTO;
import com.etm.modules.contract.dto.ContractQueryDTO;
import com.etm.modules.contract.dto.RenewalCreateDTO;
import com.etm.modules.contract.dto.RenewalQueryDTO;
import com.etm.modules.contract.dto.TerminationCreateDTO;
import com.etm.modules.contract.entity.EmploymentContract;
import com.etm.modules.contract.entity.RenewalApplication;
import com.etm.modules.contract.entity.TerminationRecord;
import com.etm.modules.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/page")
    public Result<PageResult<EmploymentContract>> getContractPage(ContractQueryDTO query) {
        return Result.ok(contractService.getContractPage(query));
    }

    @GetMapping("/{id}")
    public Result<EmploymentContract> getContractDetail(@PathVariable Long id) {
        return Result.ok(contractService.getContractDetail(id));
    }

    @PostMapping
    public Result<EmploymentContract> createContract(@RequestBody ContractCreateDTO dto) {
        return Result.ok(contractService.createContract(dto));
    }

    @PutMapping("/{id}/sign")
    public Result<Void> signContract(@PathVariable Long id) {
        contractService.signContract(id);
        return Result.ok();
    }

    @GetMapping("/renewal/page")
    public Result<PageResult<RenewalApplication>> getRenewalPage(RenewalQueryDTO query) {
        return Result.ok(contractService.getRenewalPage(query));
    }

    @PostMapping("/renewal")
    public Result<RenewalApplication> createRenewal(@RequestBody RenewalCreateDTO dto) {
        return Result.ok(contractService.createRenewal(dto));
    }

    @PutMapping("/renewal/{id}/approve")
    public Result<Void> approveRenewal(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer status = ((Number) body.get("status")).intValue();
        String comment = (String) body.get("comment");
        contractService.approveRenewal(id, status, comment);
        return Result.ok();
    }

    @GetMapping("/termination/page")
    public Result<PageResult<TerminationRecord>> getTerminationPage(
            @RequestParam Long collegeId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.ok(contractService.getTerminationPage(collegeId, pageNum, pageSize));
    }

    @PostMapping("/termination")
    public Result<TerminationRecord> createTermination(@RequestBody TerminationCreateDTO dto) {
        return Result.ok(contractService.createTermination(dto));
    }
}
