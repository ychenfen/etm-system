package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 聘用合同
 * 审批全部通过后自动生成
 */
@Data
@TableName("contract")
public class Contract {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long teacherId;
    private Long departmentId;

    /** 合同编号，如 CTR202401001 */
    private String contractNo;

    /** 关联审批 ID */
    private Long approvalId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /** 薪酬标准（元/课时） */
    private BigDecimal salaryStandard;

    /**
     * 合同状态：
     *   UNSIGNED  - 待签署
     *   SIGNED    - 已签署
     *   EXPIRED   - 已到期
     *   TERMINATED- 已终止
     */
    private String contractStatus;

    /** 教师签署时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime teacherSignTime;

    /** 学校盖章时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime schoolSignTime;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String teacherName;

    @TableField(exist = false)
    private String departmentName;
}
