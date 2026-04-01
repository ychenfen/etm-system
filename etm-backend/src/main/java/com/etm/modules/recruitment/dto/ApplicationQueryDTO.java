package com.etm.modules.recruitment.dto;

import lombok.Data;

/**
 * 招聘申请查询DTO
 */
@Data
public class ApplicationQueryDTO {

    /**
     * 页码，默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10
     */
    private Integer pageSize = 10;

    /**
     * 招聘公告ID
     */
    private Long noticeId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 最终状态: 0-待定 1-推进 2-淘汰
     */
    private Integer finalStatus;
}
