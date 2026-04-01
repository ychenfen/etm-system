package com.etm.modules.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("teacher_info")
public class TeacherInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String teacherNo;

    private String name;

    private Integer gender;

    private LocalDate birthDate;

    private String idCard;

    private String nationality;

    private String politicalStatus;

    private String education;

    private String degree;

    private String major;

    private String graduateSchool;

    private LocalDate graduateDate;

    private String title;

    private Integer titleLevel;

    private String phone;

    private String email;

    private String address;

    private String emergencyContact;

    private String emergencyPhone;

    private String bankName;

    private String bankAccount;

    private String photoUrl;

    private String resumeUrl;

    private String specialty;

    private Integer teacherStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
