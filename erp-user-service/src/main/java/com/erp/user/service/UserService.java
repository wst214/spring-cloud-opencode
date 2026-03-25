package com.erp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.user.entity.User;
import com.erp.user.entity.UserLoginDTO;
import com.erp.user.entity.UserRegisterDTO;

import java.util.Map;

/**
 * 用户服务接口
 * 
 * 定义用户相关的业务逻辑方法
 * IService<User> 是MyBatis Plus提供的服务接口，包含常用的CRUD方法
 * 
 * @param <User> 用户实体类型
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     * 
     * 业务逻辑：
     * 1. 校验用户名是否已存在
     * 2. 校验两次密码输入是否一致
     * 3. 对密码进行BCrypt加密
     * 4. 保存用户信息到数据库
     * 
     * @param registerDTO 注册信息DTO
     * @return 注册成功的用户ID
     */
    Long register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * 
     * 业务逻辑：
     * 1. 根据用户名查询用户信息
     * 2. 校验用户是否存在
     * 3. 校验用户是否被禁用
     * 4. 校验密码是否正确（使用BCrypt验证）
     * 5. 生成Token（简化处理，实际应使用JWT或OAuth2）
     * 
     * @param loginDTO 登录信息DTO
     * @return 登录成功后的用户信息（包含Token）
     */
    Map<String, Object> login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名查询用户信息
     * 
     * 用于检查用户名是否重复
     * 
     * @param username 用户名
     * @return 用户信息，如果不存在返回null
     */
    User findByUsername(String username);
    
    /**
     * 分页查询用户列表
     * 
     * 支持按用户名、姓名、手机号码等条件查询
     * 
     * @param page 页码（从1开始）
     * @param size 每页显示条数
     * @param username 用户名（模糊查询，可为空）
     * @param realName 真实姓名（模糊查询，可为空）
     * @param status 用户状态（可为空）
     * @return 分页后的用户列表
     */
    Page<User> findPage(Integer page, Integer size, String username, String realName, Integer status);
    
    /**
     * 修改用户状态
     * 
     * 用于启用或禁用用户
     * 
     * @param userId 用户ID
     * @param status 新状态（0-禁用，1-启用）
     */
    void updateStatus(Long userId, Integer status);
    
    /**
     * 修改用户密码
     * 
     * 业务逻辑：
     * 1. 校验旧密码是否正确
     * 2. 校验新密码和确认密码是否一致
     * 3. 对新密码进行BCrypt加密
     * 4. 更新密码到数据库
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword, String confirmPassword);
}
