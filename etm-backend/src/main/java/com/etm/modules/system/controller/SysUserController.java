package com.etm.modules.system.controller;

import com.etm.common.result.PageResult;
import com.etm.common.result.Result;
import com.etm.modules.system.dto.UserCreateDTO;
import com.etm.modules.system.dto.UserQueryDTO;
import com.etm.modules.system.entity.SysUser;
import com.etm.modules.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    @PreAuthorize("hasRole('admin')")
    public Result<PageResult<SysUser>> page(UserQueryDTO query) {
        return Result.success(userService.getUserPage(query));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Result<SysUser> create(@Valid @RequestBody UserCreateDTO dto) {
        return Result.success(userService.createUser(dto));
    }

    @Operation(summary = "修改用户")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysUser> update(@PathVariable Long id, @RequestBody UserCreateDTO dto) {
        return Result.success(userService.updateUser(id, dto));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @Operation(summary = "修改用户状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('admin')")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('admin')")
    public Result<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        userService.resetPassword(id, body.get("password"));
        return Result.success();
    }

    @Operation(summary = "分配角色")
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('admin')")
    public Result<?> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
        return Result.success();
    }
}
