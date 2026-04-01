package com.etm.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 课程排课
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_schedule")
public class CourseSchedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 学期
     */
    private String semester;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 学生数
     */
    private Integer studentCount;

    /**
     * 周开始
     */
    private Integer weekStart;

    /**
     * 周结束
     */
    private Integer weekEnd;

    /**
     * 星期几 (1-7)
     */
    private Integer dayOfWeek;

    /**
     * 节次开始
     */
    private Integer periodStart;

    /**
     * 节次结束
     */
    private Integer periodEnd;

    /**
     * 上课地点
     */
    private String location;

    /**
     * 总课时
     */
    private BigDecimal totalHours;

    /**
     * 考试方式
     */
    private String examMethod;

    /**
     * 状态: 0-正常 1-停课
     */
    private Integer status;

    /**
     * 教学大纲URL
     */
    private String syllabusUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
}
