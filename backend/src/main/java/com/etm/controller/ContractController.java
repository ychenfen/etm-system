package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.dto.Result;
import com.etm.entity.Contract;
import com.etm.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /** 分页查询合同 */
    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) Long teacherId,
                          @RequestParam(required = false) String status) {
        Page<Contract> page = contractService.pageList(current, size, teacherId, status);
        return Result.success(page);
    }

    /** 获取合同详情 */
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        Contract contract = contractService.getById(id);
        if (contract == null) return Result.error("合同不存在");
        return Result.success(contract);
    }

    /** 教师签署合同 */
    @PutMapping("/{id}/teacher-sign")
    public Result<?> teacherSign(@PathVariable Long id) {
        contractService.teacherSign(id);
        return Result.success("签署成功");
    }

    /** 学校盖章（完成合同签署） */
    @PutMapping("/{id}/school-sign")
    public Result<?> schoolSign(@PathVariable Long id) {
        contractService.schoolSign(id);
        return Result.success("盖章完成，合同生效");
    }
}
