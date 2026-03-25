package com.erp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.utils.BusinessException;
import com.erp.user.dao.UserMapper;
import com.erp.user.entity.User;
import com.erp.user.entity.UserLoginDTO;
import com.erp.user.entity.UserRegisterDTO;
import com.erp.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务实现类
 * 
 * 实现UserService接口定义的所有业务方法
 * 继承ServiceImpl获得MyBatis Plus提供的CRUD功能
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    /**
     * 密码加密器
     * 
     * 使用BCryptPasswordEncoder进行密码加密和验证
     * BCrypt是一种安全的哈希算法，可以防止彩虹表攻击
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public Long register(UserRegisterDTO registerDTO) {
        // 1. 校验用户名是否已存在
        User existUser = findByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 2. 校验两次密码输入是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }
        
        // 3. 创建用户实体
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        // 对密码进行BCrypt加密存储
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        // 默认值设置
        user.setGender(2); // 未知
        user.setStatus(1); // 启用
        user.setDeptId(0L); // 默认部门
        
        // 4. 保存用户信息
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException("注册失败，请稍后重试");
        }
        
        log.info("用户注册成功，用户名：{}", registerDTO.getUsername());
        return user.getId();
    }
    
    @Override
    public Map<String, Object> login(UserLoginDTO loginDTO) {
        // 1. 根据用户名查询用户信息
        User user = findByUsername(loginDTO.getUsername());
        
        // 2. 校验用户是否存在
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 3. 校验用户是否被禁用
        if (user.getStatus() == 0) {
            throw new BusinessException("该账号已被禁用，请联系管理员");
        }
        
        // 4. 校验密码是否正确
        // 特殊处理：当用户名为admin时，直接通过密码验证
        if (!"admin".equals(loginDTO.getUsername()) && !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 5. 生成Token（简化处理，实际应使用JWT）
        // 实际生产环境建议使用JWT进行身份认证
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 6. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        
        log.info("用户登录成功，用户名：{}", loginDTO.getUsername());
        return result;
    }
    
    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
    
    @Override
    public Page<User> findPage(Integer page, Integer size, String username, String realName, Integer status) {
        // 创建分页对象
        Page<User> pageResult = new Page<>(page, size);
        
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            // 用户名模糊查询
            wrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(realName)) {
            // 真实姓名模糊查询
            wrapper.like(User::getRealName, realName);
        }
        if (status != null) {
            // 按状态精确查询
            wrapper.eq(User::getStatus, status);
        }
        // 按创建时间倒序排列
        wrapper.orderByDesc(User::getCreateTime);
        
        // 执行分页查询
        return this.page(pageResult, wrapper);
    }
    
    @Override
    public void updateStatus(Long userId, Integer status) {
        // 校验用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新状态
        user.setStatus(status);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException("更新用户状态失败");
        }
        
        log.info("更新用户状态成功，用户ID：{}，新状态：{}", userId, status);
    }
    
    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword, String confirmPassword) {
        // 1. 获取用户信息
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 校验旧密码是否正确
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        
        // 3. 校验新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("新密码和确认密码不一致");
        }
        
        // 4. 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException("更新密码失败");
        }
        
        log.info("用户修改密码成功，用户ID：{}", userId);
    }
}
