package com.etm.service;

import com.etm.dto.LoginDto;
import com.etm.entity.User;

public interface AuthService {
    User login(LoginDto loginDto);
    User getUserInfo(Long userId);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
