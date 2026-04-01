package com.etm.modules.salary.dto;

import lombok.Data;

/**
 * 薪酬结算查询DTO
 */
@Data
public class SalarySettlementQueryDTO {

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
     * 结算周期 (YYYYMM)
     */
    private String settlementPeriod;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 核批状态: 0-待核 1-已核
     */
    private Integer verifyStatus;

    /**
     * 发放状态: 0-待发 1-已发
     */
    private Integer paymentStatus;
}
