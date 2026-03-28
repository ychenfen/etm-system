package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private Long departmentId;
    private Long teacherId;
    private String semester;
    private BigDecimal credit;
    private Integer hours;
    private Integer weeklyHours;
    private String className;
    private Integer studentCount;
    private String classroom;
    private String schedule;
    private String status;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String departmentName;

    @TableField(exist = false)
    private String teacherName;
}
