package com.etm.modules.system.controller;

import com.etm.common.result.Result;
import com.etm.modules.system.entity.SysMenuEntity;
import com.etm.modules.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("/list")
    public Result<List<SysMenuEntity>> getMenuList(@RequestParam(required = false) String title) {
        return Result.ok(sysMenuService.getMenuTree());
    }

    @GetMapping("/{id}")
    public Result<SysMenuEntity> getMenuDetail(@PathVariable Long id) {
        return Result.ok(sysMenuService.getById(id));
    }

    @PostMapping
    public Result<SysMenuEntity> createMenu(@RequestBody SysMenuEntity menu) {
        sysMenuService.save(menu);
        return Result.ok(menu);
    }

    @PutMapping("/{id}")
    public Result<SysMenuEntity> updateMenu(@PathVariable Long id, @RequestBody SysMenuEntity menu) {
        menu.setId(id);
        sysMenuService.updateById(menu);
        return Result.ok(menu);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return Result.ok();
    }
}
