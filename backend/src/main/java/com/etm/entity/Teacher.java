package com.etm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("teacher")
public class Teacher {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String idCard;
    private String phone;
    private String email;
    private String education;
    private String degree;
    private String title;
    private String workUnit;
    private String speciality;
    private Long departmentId;
    private String hireStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireEndDate;
    private String photo;
    private String address;
    private String bankAccount;
    private String bankName;
    private String remark;

    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String departmentName;
}
