package com.etm.modules.college.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollegeDTO {
    @NotBlank(message = "学院名称不能为空")
    private String collegeName;

    @NotBlank(message = "学院代码不能为空")
    private String collegeCode;

    @NotBlank(message = "联系人不能为空")
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotBlank(message = "邮箱不能为空")
    private String contactEmail;

    private String description;

    private Integer sort;
}
