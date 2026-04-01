package com.etm.modules.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu {

    @TableId(type = IdType.AUTO)
    private Long menuId;

    private String menuName;

    private Long parentId;

    private Integer orderNum;

    private String path;

    private String component;

    private String query;

    /** 是否外链 0否 1是 */
    private String isFrame;

    /** 是否缓存 0缓存 1不缓存 */
    private String isCache;

    /** 菜单类型 M目录 C菜单 F按钮 */
    private String menuType;

    /** 是否显示 0显示 1隐藏 */
    private String visible;

    /** 状态 0正常 1停用 */
    private String status;

    private String perms;

    private String icon;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String remark;

    @TableField(exist = false)
    private List<SysMenu> children;
}
