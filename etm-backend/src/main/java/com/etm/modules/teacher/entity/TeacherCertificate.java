package com.etm.modules.teacher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("teacher_certificate")
public class TeacherCertificate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long teacherId;

    private String certType;

    private String certName;

    private String certNo;

    private LocalDate issueDate;

    private LocalDate expireDate;

    private String issueOrg;

    private String fileUrl;

    private LocalDateTime createTime;
}
