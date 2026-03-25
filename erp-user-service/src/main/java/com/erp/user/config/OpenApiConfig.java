package com.erp.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * OpenAPI（Swagger）文档配置类
 * 
 * 配置说明：
 * - 自动生成API文档
 * - 提供统一的API文档信息
 * - 支持在线调试API接口
 * 
 * 访问地址：
 * - Swagger UI: http://localhost:8081/swagger-ui/index.html
 * - OpenAPI JSON: http://localhost:8081/v3/api-docs
 */
@Configuration
public class OpenApiConfig {
    
    /**
     * 配置OpenAPI文档信息
     * 
     * @return OpenAPI实例
     */
    @Bean
    public OpenAPI erpOpenAPI() {
        return new OpenAPI()
                // API文档信息
                .info(new Info()
                        // 文档标题
                        .title("ERP系统用户服务API文档")
                        // 文档描述
                        .description("ERP系统用户管理模块的RESTful API接口文档，提供用户注册、登录、信息管理等功能")
                        // 文档版本
                        .version("1.0.0")
                        // 联系人信息
                        .contact(new Contact()
                                .name("ERP开发团队")
                                .email("dev@erp.com")
                                .url("https://erp.com"))
                        // 许可证信息
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                        // 服务条款
                        .termsOfService("https://erp.com/terms"))
                // 服务器信息
                .servers(Collections.singletonList(
                        new Server()
                                // 服务器URL
                                .url("http://localhost:8081")
                                // 服务器描述
                                .description("本地开发服务器")
                ));
    }
}
