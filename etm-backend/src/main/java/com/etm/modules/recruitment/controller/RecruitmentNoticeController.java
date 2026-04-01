package com.etm.modules.recruitment.controller;

import com.etm.common.result.Result;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.NoticeCreateDTO;
import com.etm.modules.recruitment.dto.NoticeQueryDTO;
import com.etm.modules.recruitment.entity.RecruitmentNotice;
import com.etm.modules.recruitment.service.RecruitmentNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 招聘公告Controller
 */
@Tag(name = "招聘公告管理")
@RestController
@RequestMapping("/recruit/notice")
@RequiredArgsConstructor
public class RecruitmentNoticeController {

    private final RecruitmentNoticeService noticeService;

    @Operation(summary = "分页查询招聘公告")
    @GetMapping("/page")
    public Result<PageResult<RecruitmentNotice>> getPage(NoticeQueryDTO query) {
        PageResult<RecruitmentNotice> result = noticeService.getNoticePage(query);
        return Result.success(result);
    }

    @Operation(summary = "获取招聘公告详情")
    @GetMapping("/{id}")
    public Result<RecruitmentNotice> getById(@PathVariable Long id) {
        RecruitmentNotice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    @Operation(summary = "创建招聘公告")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<RecruitmentNotice> create(@Valid @RequestBody NoticeCreateDTO dto) {
        RecruitmentNotice notice = noticeService.createNotice(dto);
        return Result.success(notice);
    }

    @Operation(summary = "更新招聘公告")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<RecruitmentNotice> update(@PathVariable Long id, @Valid @RequestBody NoticeCreateDTO dto) {
        RecruitmentNotice notice = noticeService.updateNotice(id, dto);
        return Result.success(notice);
    }

    @Operation(summary = "删除招聘公告")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }

    @Operation(summary = "发布招聘公告")
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> publish(@PathVariable Long id) {
        noticeService.publishNotice(id);
        return Result.success();
    }
}
