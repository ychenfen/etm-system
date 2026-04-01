package com.etm.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Department;
import com.etm.entity.User;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.UserMapper;
import com.etm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Page<User> pageList(int current, int size, String keyword, String role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getRealName, keyword)
                    .or().like(User::getPhone, keyword));
        }
        if (StringUtils.hasText(role)) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(user -> {
            user.setPassword(null);
            if (user.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(user.getDepartmentId());
                if (dept != null) {
                    user.setDepartmentName(dept.getName());
                }
            }
        });
        return page;
    }

    @Override
    public void addUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(DigestUtil.md5Hex("123456"));
        this.save(user);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(null);
        this.updateById(user);
    }

    @Override
    public void resetPassword(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(DigestUtil.md5Hex("123456"));
        this.updateById(user);
    }
}
