package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.annotation.RequireRole;
import com.etm.dto.Result;
import com.etm.entity.Notice;
import com.etm.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/page")
    public Result<Page<Notice>> page(@RequestParam(defaultValue = "1") int current,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) String type) {
        return Result.success(noticeService.pageList(current, size, keyword, type));
    }

    @GetMapping("/{id}")
    public Result<Notice> getById(@PathVariable Long id) {
        return Result.success(noticeService.getById(id));
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PostMapping
    public Result<?> add(@RequestBody Notice notice) {
        noticeService.save(notice);
        return Result.success("添加成功", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @PutMapping
    public Result<?> update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success("更新成功", null);
    }

    @RequireRole({"ADMIN", "DEPARTMENT"})
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success("删除成功", null);
    }
}
