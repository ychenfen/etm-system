package com.etm.modules.recruitment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.common.utils.SecurityUtil;
import com.etm.modules.recruitment.dto.NoticeCreateDTO;
import com.etm.modules.recruitment.dto.NoticeQueryDTO;
import com.etm.modules.recruitment.entity.RecruitmentNotice;
import com.etm.modules.recruitment.mapper.RecruitmentNoticeMapper;
import com.etm.modules.recruitment.service.RecruitmentNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 招聘公告Service实现
 */
@Service
@RequiredArgsConstructor
public class RecruitmentNoticeServiceImpl extends ServiceImpl<RecruitmentNoticeMapper, RecruitmentNotice> implements RecruitmentNoticeService {

    @Override
    public PageResult<RecruitmentNotice> getNoticePage(NoticeQueryDTO query) {
        LambdaQueryWrapper<RecruitmentNotice> wrapper = new LambdaQueryWrapper<>();
        if (query.getTitle() != null) {
            wrapper.like(RecruitmentNotice::getTitle, query.getTitle());
        }
        if (query.getCollegeId() != null) {
            wrapper.eq(RecruitmentNotice::getCollegeId, query.getCollegeId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(RecruitmentNotice::getStatus, query.getStatus());
        }
        wrapper.eq(RecruitmentNotice::getDeleted, 0)
                .orderByDesc(RecruitmentNotice::getCreateTime);

        IPage<RecruitmentNotice> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public RecruitmentNotice createNotice(NoticeCreateDTO dto) {
        RecruitmentNotice notice = new RecruitmentNotice();
        notice.setTitle(dto.getTitle());
        notice.setCollegeId(dto.getCollegeId());
        notice.setContent(dto.getContent());
        notice.setRequirements(dto.getRequirements());
        notice.setApplyStartTime(dto.getApplyStartTime());
        notice.setApplyEndTime(dto.getApplyEndTime());
        notice.setStatus(0); // 草稿
        notice.setCreateBy(SecurityUtil.getCurrentUserId());
        notice.setCreateTime(LocalDateTime.now());
        notice.setDeleted(0);
        this.save(notice);
        return notice;
    }

    @Override
    public RecruitmentNotice updateNotice(Long id, NoticeCreateDTO dto) {
        RecruitmentNotice notice = this.getById(id);
        if (notice == null || notice.getDeleted() == 1) {
            throw new BusinessException("招聘公告不存在");
        }
        notice.setTitle(dto.getTitle());
        notice.setCollegeId(dto.getCollegeId());
        notice.setContent(dto.getContent());
        notice.setRequirements(dto.getRequirements());
        notice.setApplyStartTime(dto.getApplyStartTime());
        notice.setApplyEndTime(dto.getApplyEndTime());
        notice.setUpdateTime(LocalDateTime.now());
        this.updateById(notice);
        return notice;
    }

    @Override
    public void publishNotice(Long id) {
        RecruitmentNotice notice = this.getById(id);
        if (notice == null || notice.getDeleted() == 1) {
            throw new BusinessException("招聘公告不存在");
        }
        notice.setStatus(1); // 发布
        notice.setUpdateTime(LocalDateTime.now());
        this.updateById(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        RecruitmentNotice notice = this.getById(id);
        if (notice == null || notice.getDeleted() == 1) {
            throw new BusinessException("招聘公告不存在");
        }
        notice.setDeleted(1);
        notice.setUpdateTime(LocalDateTime.now());
        this.updateById(notice);
    }
}
