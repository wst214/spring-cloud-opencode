package com.erp.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.utils.PageResult;
import com.erp.common.utils.Result;
import com.erp.user.entity.User;
import com.erp.user.entity.UserLoginDTO;
import com.erp.user.entity.UserRegisterDTO;
import com.erp.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * 
 * 提供用户相关的RESTful API接口
 * 包括：用户注册、登录、查询、修改状态、修改密码等功能
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "提供用户注册、登录、信息管理等功能")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册接口
     * 
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "提供新用户注册功能")
    public Result<Long> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        Long userId = userService.register(registerDTO);
        return Result.success("注册成功", userId);
    }
    
    /**
     * 用户登录接口
     * 
     * @param loginDTO 登录信息
     * @return 登录结果，包含Token和用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户名密码，返回Token")
    public Result<Map<String, Object>> login(@Validated @RequestBody UserLoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success("登录成功", result);
    }
    
    /**
     * 根据ID查询用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户", description = "根据用户ID查询详细信息")
    public Result<User> getById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }
    
    /**
     * 分页查询用户列表
     * 
     * 支持按用户名、真实姓名、状态等条件进行模糊查询
     * 
     * @param page 页码（默认1）
     * @param size 每页条数（默认10）
     * @param username 用户名（可选，用于模糊查询）
     * @param realName 真实姓名（可选，用于模糊查询）
     * @param status 用户状态（可选，0-禁用，1-启用）
     * @return 分页后的用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "支持多条件分页查询")
    public Result<PageResult<User>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名（模糊查询）") @RequestParam(required = false) String realName,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam(required = false) Integer status) {
        Page<User> pageResult = userService.findPage(page, size, username, realName, status);
        
        // 转换为通用分页结果
        PageResult<User> result = new PageResult<>(
                pageResult.getTotal(),
                pageResult.getRecords(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
        
        return Result.success(result);
    }
    
    /**
     * 查询所有启用状态的用户
     * 
     * @return 所有启用的用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有用户", description = "查询所有启用状态的用户列表")
    public Result<List<User>> getAll() {
        List<User> users = userService.list();
        return Result.success(users);
    }
    
    /**
     * 修改用户状态
     * 
     * @param id 用户ID
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "修改用户状态", description = "启用或禁用用户账号")
    public Result<Void> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success("状态更新成功", null);
    }
    
    /**
     * 修改用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     * @return 操作结果
     */
    @PutMapping("/password/{userId}")
    @Operation(summary = "修改密码", description = "用户修改自己的登录密码")
    public Result<Void> updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            @Parameter(description = "确认密码") @RequestParam String confirmPassword) {
        userService.updatePassword(userId, oldPassword, newPassword, confirmPassword);
        return Result.success("密码修改成功", null);
    }
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户（物理删除）")
    public Result<Void> delete(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        boolean result = userService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }
    
    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "更新用户信息", description = "修改用户的基本信息")
    public Result<Void> update(@RequestBody User user) {
        if (user.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        User existUser = userService.getById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        user.setUsername(null);
        boolean result = userService.updateById(user);
        return result ? Result.success("更新成功", null) : Result.error("更新失败");
    }
}
