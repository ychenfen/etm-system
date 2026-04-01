package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.User;

public interface UserService extends IService<User> {
    Page<User> pageList(int current, int size, String keyword, String role);
    void addUser(User user);
    void updateUser(User user);
    void resetPassword(Long userId);
}
