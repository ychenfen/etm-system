package com.etm.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.system.dto.UserCreateDTO;
import com.etm.modules.system.dto.UserQueryDTO;
import com.etm.modules.system.entity.SysUser;
import com.etm.modules.system.mapper.SysUserMapper;
import com.etm.modules.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<SysUser> getUserPage(UserQueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeleted, 0)
                .like(StringUtils.hasText(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StringUtils.hasText(query.getRealName()), SysUser::getRealName, query.getRealName())
                .like(StringUtils.hasText(query.getPhone()), SysUser::getPhone, query.getPhone())
                .eq(query.getStatus() != null, SysUser::getStatus, query.getStatus())
                .eq(query.getCollegeId() != null, SysUser::getCollegeId, query.getCollegeId())
                .eq(query.getUserType() != null, SysUser::getUserType, query.getUserType())
                .orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> page = page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return PageResult.of(page);
    }

    @Override
    @Transactional
    public SysUser createUser(UserCreateDTO dto) {
        long count = count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername())
                .eq(SysUser::getDeleted, 0));
        if (count > 0) {
            throw BusinessException.of("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUserType(dto.getUserType() != null ? dto.getUserType() : 0);
        user.setCollegeId(dto.getCollegeId());
        user.setStatus(1);
        user.setDeleted(0);
        save(user);
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            assignRoles(user.getId(), dto.getRoleIds());
        }
        return user;
    }

    @Override
    @Transactional
    public SysUser updateUser(Long id, UserCreateDTO dto) {
        SysUser user = getById(id);
        if (user == null) throw BusinessException.of(1002, "用户不存在");
        if (StringUtils.hasText(dto.getRealName())) user.setRealName(dto.getRealName());
        if (StringUtils.hasText(dto.getPhone())) user.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getEmail())) user.setEmail(dto.getEmail());
        if (dto.getCollegeId() != null) user.setCollegeId(dto.getCollegeId());
        updateById(user);
        if (dto.getRoleIds() != null) {
            assignRoles(id, dto.getRoleIds());
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        SysUser user = getById(id);
        if (user == null) throw BusinessException.of(1002, "用户不存在");
        user.setDeleted(1);
        updateById(user);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = getById(id);
        if (user == null) throw BusinessException.of(1002, "用户不存在");
        user.setStatus(status);
        updateById(user);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        SysUser user = getById(id);
        if (user == null) throw BusinessException.of(1002, "用户不存在");
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        jdbcTemplate.update("DELETE FROM sys_user_role WHERE user_id = ?", userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                jdbcTemplate.update("INSERT INTO sys_user_role(user_id, role_id) VALUES(?, ?)", userId, roleId);
            }
        }
    }
}
