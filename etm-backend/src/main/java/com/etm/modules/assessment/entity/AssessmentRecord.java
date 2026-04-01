package com.etm.modules.assessment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 考核记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("assessment_record")
public class AssessmentRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 考核周期ID
     */
    private Long periodId;

    /**
     * 教学质量评分
     */
    private BigDecimal teachingQualityScore;

    /**
     * 出勤评分
     */
    private BigDecimal attendanceScore;

    /**
     * 贡献评分
     */
    private BigDecimal contributionScore;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 等级: 优秀/合格/不合格
     */
    private String grade;

    /**
     * 报告URL
     */
    private String reportUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
