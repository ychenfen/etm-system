package com.etm.modules.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 招聘公告创建/更新DTO
 */
@Data
public class NoticeCreateDTO {

    /**
     * 公告标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 学院ID
     */
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    /**
     * 公告内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 岗位要求
     */
    @NotBlank(message = "岗位要求不能为空")
    private String requirements;

    /**
     * 申报开始时间
     */
    @NotNull(message = "申报开始时间不能为空")
    private LocalDateTime applyStartTime;

    /**
     * 申报结束时间
     */
    @NotNull(message = "申报结束时间不能为空")
    private LocalDateTime applyEndTime;
}
