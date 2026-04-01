package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("salary")
public class Salary {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teacherId;
    @TableField("`month`")
    private String month;
    private BigDecimal baseSalary;
    private BigDecimal totalHours;
    private BigDecimal hourRate;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private BigDecimal totalSalary;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;
    private String remark;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String teacherName;
}
