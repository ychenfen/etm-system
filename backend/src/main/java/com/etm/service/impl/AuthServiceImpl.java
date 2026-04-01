package com.etm.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.dto.LoginDto;
import com.etm.entity.Department;
import com.etm.entity.User;
import com.etm.entity.Teacher;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.mapper.UserMapper;
import com.etm.service.AuthService;
import com.etm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User login(LoginDto loginDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }
        String md5Password = DigestUtil.md5Hex(loginDto.getPassword());
        if (!md5Password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        user.setToken(token);
        user.setPassword(null);
        populateUserExtra(user);
        return user;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(null);
        populateUserExtra(user);
        return user;
    }

    private void populateUserExtra(User user) {
        if (user.getDepartmentId() != null) {
            Department dept = departmentMapper.selectById(user.getDepartmentId());
            if (dept != null) {
                user.setDepartmentName(dept.getName());
            }
        }
        // 如果是教师角色，查找关联的教师ID
        if ("TEACHER".equals(user.getRole())) {
            LambdaQueryWrapper<Teacher> tw = new LambdaQueryWrapper<>();
            tw.eq(Teacher::getUserId, user.getId());
            Teacher teacher = teacherMapper.selectOne(tw);
            if (teacher != null) {
                user.setTeacherId(teacher.getId());
            }
        }
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String md5OldPassword = DigestUtil.md5Hex(oldPassword);
        if (!md5OldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(DigestUtil.md5Hex(newPassword));
        userMapper.updateById(user);
    }
}
