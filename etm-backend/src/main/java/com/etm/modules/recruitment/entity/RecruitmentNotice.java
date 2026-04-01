package com.etm.modules.recruitment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.time.LocalDateTime;

/**
 * 招聘公告
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recruitment_notice")
public class RecruitmentNotice {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 所属学院ID
     */
    private Long collegeId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 岗位要求
     */
    private String requirements;

    /**
     * 申报开始时间
     */
    private LocalDateTime applyStartTime;

    /**
     * 申报结束时间
     */
    private LocalDateTime applyEndTime;

    /**
     * 状态: 0-草稿 1-发布 2-结束
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createBy;

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
