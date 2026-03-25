package com.erp.user.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录请求DTO
 * 
 * 用于接收用户登录请求的数据
 */
@Data
public class UserLoginDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     * 
     * 不能为空
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     * 
     * 不能为空
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
