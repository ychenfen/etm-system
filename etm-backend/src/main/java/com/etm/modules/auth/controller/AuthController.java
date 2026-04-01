package com.etm.modules.auth.controller;

import com.etm.common.result.Result;
import com.etm.common.utils.JwtUtil;
import com.etm.common.utils.SecurityUtil;
import com.etm.modules.auth.dto.LoginDTO;
import com.etm.modules.system.entity.SysUser;
import com.etm.modules.system.mapper.SysUserMapper;
import com.etm.security.service.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SysUserMapper userMapper;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            // Update last login info
            SysUser user = new SysUser();
            user.setId(loginUser.getUserId());
            user.setLoginTime(LocalDateTime.now());
            userMapper.updateById(user);

            String accessToken = jwtUtil.generateToken(loginUser.getUsername(), loginUser.getUserId());
            String refreshToken = jwtUtil.generateRefreshToken(loginUser.getUsername());

            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", accessToken);
            data.put("refreshToken", refreshToken);
            data.put("tokenType", "Bearer");
            data.put("expiresIn", jwtUtil.getExpiration());

            return Result.success(data);
        } catch (DisabledException e) {
            return Result.error(2003, "用户已被禁用");
        } catch (BadCredentialsException e) {
            return Result.error(2002, "用户名或密码错误");
        }
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<LoginUser> getInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        return Result.success(loginUser);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出成功");
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<?> refresh(@RequestParam String refreshToken) {
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        // Find user id
        var user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username));
        if (user == null) return Result.error(2001, "用户不存在");

        String newToken = jwtUtil.generateToken(username, user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", newToken);
        data.put("tokenType", "Bearer");
        data.put("expiresIn", jwtUtil.getExpiration());
        return Result.success(data);
    }
}
