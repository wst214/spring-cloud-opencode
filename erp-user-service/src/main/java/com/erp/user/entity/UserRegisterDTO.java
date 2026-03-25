package com.erp.user.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户注册请求DTO
 * 
 * DTO设计说明：
 * - Data Transfer Object，用于接收前端提交的注册请求数据
 * - 与实体类的区别：只包含需要校验的字段，不包含数据库表的所有字段
 * - 通过@NotBlank等注解进行参数校验
 */
@Data
public class UserRegisterDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     * 
     * 校验规则：
     * - 不能为空（@NotBlank）
     * - 长度在3-50个字符之间
     * - 唯一性由后端业务逻辑保证
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度在3-50个字符之间")
    private String username;
    
    /**
     * 密码
     * 
     * 校验规则：
     * - 不能为空
     * - 长度在6-100个字符之间
     * - 实际存储时会被BCrypt加密
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度在6-100个字符之间")
    private String password;
    
    /**
     * 确认密码
     * 
     * 用于两次密码输入一致性校验
     * 注意：这个字段不存入数据库，仅用于表单验证
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    /**
     * 真实姓名
     * 
     * 校验规则：长度在2-50个字符之间
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "真实姓名长度在2-50个字符之间")
    private String realName;
    
    /**
     * 手机号码
     * 
     * 校验规则：
     * - 不能为空
     * - 长度在11个字符
     */
    @NotBlank(message = "手机号码不能为空")
    @Size(min = 11, max = 20, message = "手机号码长度不正确")
    private String phone;
    
    /**
     * 邮箱
     * 
     * 校验规则：
     * - 不能为空
     * - 必须符合邮箱格式（@Email）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
