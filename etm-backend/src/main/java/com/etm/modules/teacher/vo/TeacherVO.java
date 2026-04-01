package com.etm.modules.teacher.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TeacherVO {
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String collegeName;
}
