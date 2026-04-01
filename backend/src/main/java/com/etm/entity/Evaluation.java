package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("evaluation")
public class Evaluation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teacherId;
    private Long courseId;
    private String semester;
    private BigDecimal teachingScore;
    private BigDecimal attendanceScore;
    private BigDecimal studentScore;
    private BigDecimal totalScore;
    private String evaluator;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate evaluationDate;
    private String comment;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String teacherName;

    @TableField(exist = false)
    private String courseName;
}
