package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.dto.Result;
import com.etm.entity.Salary;
import com.etm.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/page")
    public Result<Page<Salary>> page(@RequestParam(defaultValue = "1") int current,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) Long teacherId,
                                     @RequestParam(required = false) String month,
                                     @RequestParam(required = false) String status) {
        return Result.success(salaryService.pageList(current, size, teacherId, month, status));
    }

    @GetMapping("/{id}")
    public Result<Salary> getById(@PathVariable Long id) {
        return Result.success(salaryService.getById(id));
    }

    @PostMapping
    public Result<?> add(@RequestBody Salary salary) {
        salaryService.save(salary);
        return Result.success("添加成功", null);
    }

    @PutMapping
    public Result<?> update(@RequestBody Salary salary) {
        salaryService.updateById(salary);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        salaryService.removeById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/approve/{id}")
    public Result<?> approve(@PathVariable Long id) {
        salaryService.approve(id);
        return Result.success("审核通过", null);
    }

    @PutMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        salaryService.pay(id);
        return Result.success("已发放", null);
    }
}
