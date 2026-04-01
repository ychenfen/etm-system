package com.etm.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.system.dto.UserCreateDTO;
import com.etm.modules.system.dto.UserQueryDTO;
import com.etm.modules.system.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    PageResult<SysUser> getUserPage(UserQueryDTO query);

    SysUser createUser(UserCreateDTO dto);

    SysUser updateUser(Long id, UserCreateDTO dto);

    void deleteUser(Long id);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id, String newPassword);

    void assignRoles(Long userId, java.util.List<Long> roleIds);
}
