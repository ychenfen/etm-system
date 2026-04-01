package com.etm.modules.teacher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TeacherCreateDTO {
    @NotBlank(message = "姓名不能为空")
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

    @NotBlank(message = "手机号不能为空")
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

    private Long collegeId;

    private List<WorkExperienceDTO> workExperiences;

    private List<CertificateDTO> certificates;
}
