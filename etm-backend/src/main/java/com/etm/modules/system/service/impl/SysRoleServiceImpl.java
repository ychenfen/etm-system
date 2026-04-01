package com.etm.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.modules.system.entity.SysRole;
import com.etm.modules.system.mapper.SysRoleMapper;
import com.etm.modules.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SysRole> getAllRoles() {
        return list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getDeleted, 0)
                .orderByAsc(SysRole::getSort)
                .orderByDesc(SysRole::getCreateTime));
    }

    @Override
    public SysRole getRoleById(Long id) {
        SysRole role = getById(id);
        if (role == null || role.getDeleted() == 1) {
            throw BusinessException.of("角色不存在");
        }
        return role;
    }

    @Override
    @Transactional
    public SysRole createRole(SysRole role) {
        long count = count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, role.getRoleCode())
                .eq(SysRole::getDeleted, 0));
        if (count > 0) {
            throw BusinessException.of("角色代码已存在");
        }

        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        save(role);
        return role;
    }

    @Override
    @Transactional
    public SysRole updateRole(SysRole role) {
        SysRole existRole = getById(role.getId());
        if (existRole == null || existRole.getDeleted() == 1) {
            throw BusinessException.of("角色不存在");
        }

        long count = count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, role.getRoleCode())
                .ne(SysRole::getId, role.getId())
                .eq(SysRole::getDeleted, 0));
        if (count > 0) {
            throw BusinessException.of("角色代码已存在");
        }

        updateById(role);
        return role;
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        SysRole role = getById(id);
        if (role == null || role.getDeleted() == 1) {
            throw BusinessException.of("角色不存在");
        }
        removeById(id);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> menuIds) {
        SysRole role = getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw BusinessException.of("角色不存在");
        }

        // Delete existing permissions
        jdbcTemplate.update("DELETE FROM sys_role_menu WHERE role_id = ?", roleId);

        // Insert new permissions
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                jdbcTemplate.update(
                        "INSERT INTO sys_role_menu(role_id, menu_id) VALUES(?, ?)",
                        roleId, menuId
                );
            }
        }
    }
}
