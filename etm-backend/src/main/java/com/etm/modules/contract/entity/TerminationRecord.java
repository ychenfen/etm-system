package com.etm.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("termination_record")
public class TerminationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("contract_id")
    private Long contractId;

    @TableField("termination_type")
    private Integer terminationType;

    @TableField("termination_reason")
    private String terminationReason;

    @TableField("termination_date")
    private LocalDate terminationDate;

    @TableField("notice_file_url")
    private String noticeFileUrl;

    @TableField("handover_status")
    private Integer handoverStatus;

    @TableField("handover_details")
    private String handoverDetails;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
