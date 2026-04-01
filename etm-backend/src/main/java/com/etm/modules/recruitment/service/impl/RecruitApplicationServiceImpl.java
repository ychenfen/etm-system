package com.etm.modules.recruitment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApplicationQueryDTO;
import com.etm.modules.recruitment.dto.InterviewDTO;
import com.etm.modules.recruitment.dto.SecondReviewDTO;
import com.etm.modules.recruitment.entity.RecruitApplication;
import com.etm.modules.recruitment.mapper.RecruitApplicationMapper;
import com.etm.modules.recruitment.service.RecruitApplicationService;
import com.etm.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 招聘申请Service实现
 */
@Service
@RequiredArgsConstructor
public class RecruitApplicationServiceImpl extends ServiceImpl<RecruitApplicationMapper, RecruitApplication> implements RecruitApplicationService {

    @Override
    public PageResult<RecruitApplication> getApplicationPage(ApplicationQueryDTO query) {
        LambdaQueryWrapper<RecruitApplication> wrapper = new LambdaQueryWrapper<>();
        if (query.getNoticeId() != null) {
            wrapper.eq(RecruitApplication::getNoticeId, query.getNoticeId());
        }
        if (query.getTeacherId() != null) {
            wrapper.eq(RecruitApplication::getTeacherId, query.getTeacherId());
        }
        if (query.getFinalStatus() != null) {
            wrapper.eq(RecruitApplication::getFinalStatus, query.getFinalStatus());
        }
        wrapper.orderByDesc(RecruitApplication::getApplyTime);

        IPage<RecruitApplication> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public RecruitApplication createApplication(Long noticeId, Long teacherId, String selfIntroduction) {
        RecruitApplication application = new RecruitApplication();
        application.setNoticeId(noticeId);
        application.setTeacherId(teacherId);
        application.setSelfIntroduction(selfIntroduction);
        application.setApplyTime(LocalDateTime.now());
        application.setMatchScore(new BigDecimal("80")); // 默认匹配度80
        application.setFirstReviewStatus(0); // 待审
        application.setSecondReviewStatus(0); // 待审
        application.setFinalStatus(0); // 待定
        this.save(application);
        return application;
    }

    @Override
    public void doFirstReview(Long id) {
        RecruitApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException("招聘申请不存在");
        }
        // 自动匹配，设置matchScore并设firstReviewStatus=1
        application.setMatchScore(new BigDecimal("85")); // 自动匹配评分
        application.setFirstReviewStatus(1); // 通过
        this.updateById(application);
    }

    @Override
    public void doSecondReview(Long id, SecondReviewDTO dto) {
        RecruitApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException("招聘申请不存在");
        }
        application.setSecondReviewStatus(dto.getStatus());
        application.setSecondReviewComment(dto.getRemark());
        application.setSecondReviewBy(SecurityUtil.getCurrentUserId());
        application.setSecondReviewTime(LocalDateTime.now());
        this.updateById(application);
    }

    @Override
    public void submitInterview(Long id, InterviewDTO dto) {
        RecruitApplication application = this.getById(id);
        if (application == null) {
            throw new BusinessException("招聘申请不存在");
        }
        // 计算总分=各项相加
        int totalScore = dto.getTeachingConcept() + dto.getProfessionalAbility()
                        + dto.getExpressionAbility() + dto.getPracticalExperience()
                        + dto.getOverallImpression();
        application.setInterviewScore(new BigDecimal(totalScore));
        application.setInterviewComment(dto.getEvaluation());
        // 根据推荐判断最终状态 1-推进 2-淘汰
        application.setFinalStatus(dto.getIsRecommend() != null && dto.getIsRecommend() == 1 ? 1 : 2);
        this.updateById(application);
    }
}
