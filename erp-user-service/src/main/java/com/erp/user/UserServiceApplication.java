package com.erp.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 * 
 * Spring Boot应用启动入口
 * 使用@SpringBootApplication注解标注这是一个Spring Boot应用
 * 
 * 组件扫描说明：
 * - @ComponentScan：扫描当前包及其子包下的组件
 * - @MapperScan：扫描MyBatis的Mapper接口
 * 
 * Nacos集成：
 * - 服务启动时自动注册到Nacos服务注册中心
 * - 从Nacos配置中心读取配置文件
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.erp.user", "com.erp.common"})
@MapperScan("com.erp.user.dao")
public class UserServiceApplication {
    
    /**
     * 应用启动入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("===========================================");
        System.out.println("用户服务启动成功！");
        System.out.println("API文档地址：http://localhost:8081/swagger-ui/index.html");
        System.out.println("===========================================");
    }
}
