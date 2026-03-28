package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.dto.Result;
import com.etm.entity.Evaluation;
import com.etm.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/page")
    public Result<Page<Evaluation>> page(@RequestParam(defaultValue = "1") int current,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) Long teacherId,
                                          @RequestParam(required = false) String semester) {
        return Result.success(evaluationService.pageList(current, size, teacherId, semester));
    }

    @GetMapping("/{id}")
    public Result<Evaluation> getById(@PathVariable Long id) {
        return Result.success(evaluationService.getById(id));
    }

    @PostMapping
    public Result<?> add(@RequestBody Evaluation evaluation) {
        evaluationService.save(evaluation);
        return Result.success("添加成功", null);
    }

    @PutMapping
    public Result<?> update(@RequestBody Evaluation evaluation) {
        evaluationService.updateById(evaluation);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        evaluationService.removeById(id);
        return Result.success("删除成功", null);
    }
}
