package com.etm.modules.college.dto;

import lombok.Data;

@Data
public class CollegeQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String collegeName;
    private Integer status;
}
