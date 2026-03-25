package com.erp.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品服务启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.erp.product", "com.erp.common"})
@MapperScan("com.erp.product.dao")
public class ProductServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        System.out.println("===========================================");
        System.out.println("商品服务启动成功！");
        System.out.println("API文档地址：http://localhost:8082/swagger-ui/index.html");
        System.out.println("===========================================");
    }
}
