package com.etm.modules.recruitment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApplicationQueryDTO;
import com.etm.modules.recruitment.dto.InterviewDTO;
import com.etm.modules.recruitment.dto.SecondReviewDTO;
import com.etm.modules.recruitment.entity.RecruitApplication;

/**
 * 招聘申请Service接口
 */
public interface RecruitApplicationService extends IService<RecruitApplication> {

    /**
     * 分页查询招聘申请
     */
    PageResult<RecruitApplication> getApplicationPage(ApplicationQueryDTO query);

    /**
     * 创建招聘申请
     */
    RecruitApplication createApplication(Long noticeId, Long teacherId, String selfIntroduction);

    /**
     * 初审，自动匹配，设置matchScore并设firstReviewStatus=1
     */
    void doFirstReview(Long id);

    /**
     * 复审
     */
    void doSecondReview(Long id, SecondReviewDTO dto);

    /**
     * 提交面试评估，计算总分=各项相加，存入interviewScore
     */
    void submitInterview(Long id, InterviewDTO dto);
}
