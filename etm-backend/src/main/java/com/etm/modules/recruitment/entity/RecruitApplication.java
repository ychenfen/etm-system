package com.etm.modules.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 招聘申请
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recruit_application")
public class RecruitApplication {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 招聘公告ID
     */
    private Long noticeId;

    /**
     * 申请教师ID
     */
    private Long teacherId;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 自我介绍
     */
    private String selfIntroduction;

    /**
     * 匹配度评分
     */
    private BigDecimal matchScore;

    /**
     * 初审状态: 0-待审 1-通过 2-不通过
     */
    private Integer firstReviewStatus;

    /**
     * 初审意见
     */
    private String firstReviewComment;

    /**
     * 复审状态: 0-待审 1-通过 2-不通过
     */
    private Integer secondReviewStatus;

    /**
     * 复审意见
     */
    private String secondReviewComment;

    /**
     * 复审人ID
     */
    private Long secondReviewBy;

    /**
     * 复审时间
     */
    private LocalDateTime secondReviewTime;

    /**
     * 面试评分
     */
    private BigDecimal interviewScore;

    /**
     * 面试意见
     */
    private String interviewComment;

    /**
     * 最终状态: 0-待定 1-推进 2-淘汰
     */
    private Integer finalStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
