package com.etm.controller;

import com.etm.dto.LoginDto;
import com.etm.dto.PasswordDto;
import com.etm.dto.Result;
import com.etm.entity.User;
import com.etm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginDto loginDto) {
        User user = authService.login(loginDto);
        return Result.success("登录成功", user);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = authService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        authService.changePassword(userId, passwordDto.getOldPassword(), passwordDto.getNewPassword());
        return Result.success("密码修改成功", null);
    }
}
