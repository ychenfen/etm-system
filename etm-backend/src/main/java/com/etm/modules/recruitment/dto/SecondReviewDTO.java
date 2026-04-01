package com.etm.modules.recruitment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 复审DTO
 */
@Data
public class SecondReviewDTO {

    /**
     * 复审状态: 1-通过 2-不通过
     */
    @NotNull(message = "复审状态不能为空")
    private Integer status;

    /**
     * 复审意见
     */
    private String remark;
}
