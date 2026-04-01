package com.etm.modules.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_role")
public class SysRole {

    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String roleName;

    private String roleKey;

    private Integer roleSort;

    /** 数据范围 1全部 2自定义 3本部门 4本部门及以下 */
    private String dataScope;

    private Boolean menuCheckStrictly;

    private Boolean deptCheckStrictly;

    /** 状态 0正常 1停用 */
    private String status;

    @TableLogic
    private String delFlag;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String remark;
}
