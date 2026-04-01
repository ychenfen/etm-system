package com.etm.modules.teacher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("teacher_work_experience")
public class TeacherWorkExperience {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long teacherId;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    private String workContent;

    private String leaveReason;

    private LocalDateTime createTime;
}
