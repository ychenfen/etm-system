package com.etm.modules.teacher.dto;

import lombok.Data;

@Data
public class TeacherQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
    private String phone;
    private Integer teacherStatus;
    private Long collegeId;
    private Integer titleLevel;
}
