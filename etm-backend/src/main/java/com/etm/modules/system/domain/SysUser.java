package com.etm.modules.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private Long deptId;

    private String username;

    @JsonIgnore
    private String password;

    private String nickName;

    private String userType;

    private String email;

    private String phone;

    private String sex;

    private String avatar;

    /** 状态 0正常 1停用 */
    private String status;

    @TableLogic
    private String delFlag;

    private String loginIp;

    private LocalDateTime loginDate;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String remark;

    /** 非数据库字段 */
    @TableField(exist = false)
    private String roleName;
}
