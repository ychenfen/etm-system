package com.etm.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 学生评教
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("student_evaluation")
public class StudentEvaluation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程排课ID
     */
    private Long scheduleId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 清晰度评分 (1-5)
     */
    private Integer clarityScore;

    /**
     * 互动评分 (1-5)
     */
    private Integer interactionScore;

    /**
     * 作业评分 (1-5)
     */
    private Integer homeworkScore;

    /**
     * 态度评分 (1-5)
     */
    private Integer attitudeScore;

    /**
     * 总体评分 (1-5)
     */
    private Integer overallScore;

    /**
     * 评价意见
     */
    private String comments;

    /**
     * 是否匿名: 1-是 0-否
     */
    private Integer isAnonymous;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
