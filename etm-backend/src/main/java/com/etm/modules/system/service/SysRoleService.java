package com.etm.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.modules.system.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<SysRole> getAllRoles();

    SysRole getRoleById(Long id);

    SysRole createRole(SysRole role);

    SysRole updateRole(SysRole role);

    void deleteRole(Long id);

    void assignPermissions(Long roleId, List<Long> menuIds);
}
