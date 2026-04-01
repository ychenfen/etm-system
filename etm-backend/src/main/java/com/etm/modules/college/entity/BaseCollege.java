package com.etm.modules.college.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("base_college")
public class BaseCollege {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String collegeName;

    private String collegeCode;

    private String contactPerson;

    private String contactPhone;

    private String contactEmail;

    private String description;

    private Integer sort;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
