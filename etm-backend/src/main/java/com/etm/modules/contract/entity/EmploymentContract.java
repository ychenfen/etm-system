package com.etm.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("employment_contract")
public class EmploymentContract implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("college_id")
    private Long collegeId;

    @TableField("college_name")
    private String collegeName;

    @TableField("contract_no")
    private String contractNo;

    @TableField("employ_start_date")
    private LocalDate employStartDate;

    @TableField("employ_end_date")
    private LocalDate employEndDate;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("contract_file_url")
    private String contractFileUrl;

    @TableField("sign_status")
    private Integer signStatus;

    @TableField("sign_time")
    private LocalDateTime signTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
