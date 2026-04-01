package com.etm.modules.course.dto;

import lombok.Data;

/**
 * 课程排课查询DTO
 */
@Data
public class ScheduleQueryDTO {

    /**
     * 页码，默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10
     */
    private Integer pageSize = 10;

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
}
