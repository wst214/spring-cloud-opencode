package com.erp.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全配置类
 * 配置密码加密器和安全相关组件
 */
@Configuration
public class SecurityConfig {

    /**
     * 配置密码编码器
     * 使用BCrypt算法对密码进行加密和验证
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
