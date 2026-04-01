package com.etm.modules.system.controller;

import com.etm.common.result.Result;
import com.etm.modules.system.entity.SysRole;
import com.etm.modules.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统角色管理")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "获取所有角色")
    @GetMapping("/list")
    @PreAuthorize("hasRole('admin')")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.getAllRoles());
    }

    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(roleService.getRoleById(id));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Result<SysRole> create(@Valid @RequestBody SysRole role) {
        return Result.success(roleService.createRole(role));
    }

    @Operation(summary = "修改角色")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysRole> update(@PathVariable Long id, @Valid @RequestBody SysRole role) {
        role.setId(id);
        return Result.success(roleService.updateRole(role));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "分配菜单权限")
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasRole('admin')")
    public Result<?> assignPermissions(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        roleService.assignPermissions(id, menuIds);
        return Result.success("分配成功");
    }
}
