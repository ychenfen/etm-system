package com.etm.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教学督导
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("teaching_supervision")
public class TeachingSupervision {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程排课ID
     */
    private Long scheduleId;

    /**
     * 督导人ID
     */
    private Long supervisorId;

    /**
     * 督导日期
     */
    private LocalDate supervisionDate;

    /**
     * 是否按时: 1-是 0-否
     */
    private Integer isOnTime;

    /**
     * 内容一致性 (1-5)
     */
    private Integer contentConsistency;

    /**
     * 教学方法 (1-5)
     */
    private Integer teachingMethod;

    /**
     * 课堂氛围 (1-5)
     */
    private Integer classroomAtmosphere;

    /**
     * 学生反馈 (1-5)
     */
    private Integer studentFeedback;

    /**
     * 问题说明
     */
    private String problems;

    /**
     * 改进建议
     */
    private String suggestions;

    /**
     * 证明文件URLs (JSON数组)
     */
    private String evidenceUrls;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
