package com.etm.modules.salary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 薪酬结算核批DTO
 */
@Data
public class VerifyDTO {

    /**
     * 核批类型: verify(核批) 或 pay(发放)
     */
    @NotNull(message = "核批类型不能为空")
    private String verifyType;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
