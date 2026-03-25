package com.erp.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单服务启动类
 * 
 * @EnableFeignClients：启用Feign客户端，进行服务间调用
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.erp.order", "com.erp.common"})
@MapperScan("com.erp.order.dao")
@EnableFeignClients(basePackages = {"com.erp.order.feign"})
public class OrderServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("===========================================");
        System.out.println("订单服务启动成功！");
        System.out.println("API文档地址：http://localhost:8083/swagger-ui/index.html");
        System.out.println("===========================================");
        System.out.println("666");
    }
}
