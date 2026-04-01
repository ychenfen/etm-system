package com.etm.modules.recruitment.dto;

import lombok.Data;

/**
 * 招聘公告查询DTO
 */
@Data
public class NoticeQueryDTO {

    /**
     * 页码，默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10
     */
    private Integer pageSize = 10;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 状态: 0-草稿 1-发布 2-结束
     */
    private Integer status;
}
