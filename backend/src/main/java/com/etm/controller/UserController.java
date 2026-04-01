package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.annotation.RequireRole;
import com.etm.dto.Result;
import com.etm.entity.User;
import com.etm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequireRole({"ADMIN"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") int current,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String role) {
        return Result.success(userService.pageList(current, size, keyword, role));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping
    public Result<?> add(@RequestBody User user) {
        userService.addUser(user);
        return Result.success("添加成功", null);
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/reset-password/{id}")
    public Result<?> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success("密码已重置为123456", null);
    }

    @PutMapping("/status/{id}")
    public Result<?> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userService.updateById(user);
        return Result.success("状态更新成功", null);
    }
}
