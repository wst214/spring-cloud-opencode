# Spring Cloud ERP系统启动说明

## 项目概述

这是一个基于Spring Cloud的简单ERP系统，使用Nacos作为服务注册中心和配置中心。

## 技术栈

- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.8
- **Spring Cloud Alibaba**: 2021.0.5.0
- **Nacos**: 2.x
- **MyBatis Plus**: 3.5.3.1
- **MySQL**: 8.0
- **Gateway**: Spring Cloud Gateway

## 项目结构

```
spring-cloud-opencode/
├── pom.xml                          # 父POM文件
├── docker-compose.yml               # Docker Compose编排文件
├── start-docker.sh                  # Linux/Mac启动脚本
├── start-docker.bat                 # Windows启动脚本
├── README.md                        # 项目说明文档
├── erp-common/                      # 通用模块（公共代码）
├── erp-user-service/                # 用户服务（端口：8081）
├── erp-product-service/             # 商品服务（端口：8082）
├── erp-order-service/              # 订单服务（端口：8083）
├── erp-gateway/                     # 网关服务（端口：8080）
└── database/                        # 数据库脚本
    └── erp-init.sql                # 数据库初始化脚本
```

## 快速启动（使用Docker）

### 前置条件

1. 安装Docker Desktop for Windows
   - 下载地址：https://www.docker.com/products/docker-desktop
   - 安装完成后启动Docker Desktop

2. 检查Docker是否正常运行
   ```bash
   docker --version
   docker-compose --version
   ```

### 启动步骤

#### 方法一：使用启动脚本（推荐）

1. 打开终端（Windows PowerShell或CMD）

2. 进入项目目录
   ```bash
   cd D:\workspace\eclipse-workspace\spring-cloud-opencode
   ```

3. 运行启动脚本
   ```bash
   # Windows
   start-docker.bat
   
   # Linux/Mac
   chmod +x start-docker.sh
   ./start-docker.sh
   ```

#### 方法二：手动启动

1. 进入项目目录
   ```bash
   cd D:\workspace\eclipse-workspace\spring-cloud-opencode
   ```

2. 启动所有服务
   ```bash
   docker-compose up -d --build
   ```

3. 查看服务状态
   ```bash
   docker-compose ps
   ```

4. 查看日志
   ```bash
   docker-compose logs -f
   ```

### 服务访问地址

启动成功后，访问以下地址：

| 服务 | 地址 | 说明 |
|------|------|------|
| **Nacos控制台** | http://localhost:8848/nacos | 服务注册与配置中心 |
| **API网关** | http://localhost:8080 | 统一入口 |
| **用户服务API** | http://localhost:8081/swagger-ui/index.html | 用户管理 |
| **商品服务API** | http://localhost:8082/swagger-ui/index.html | 商品管理 |
| **订单服务API** | http://localhost:8083/swagger-ui/index.html | 订单管理 |

**Nacos账号**: nacos / nacos123456

### 常用Docker命令

```bash
# 查看所有容器
docker-compose ps

# 查看日志
docker-compose logs -f [服务名]

# 停止所有服务
docker-compose down

# 重新启动
docker-compose restart

# 进入容器
docker exec -it erp-user-service bash

# 查看容器IP
docker inspect erp-user-service | grep IPAddress

# 删除所有容器和镜像
docker-compose down --rmi all -v
```

### 注意事项

1. **首次启动**：首次启动会下载基础镜像（MySQL、Nacos）和编译Java项目，需要等待较长时间（10-30分钟）

2. **端口占用**：确保以下端口未被占用：3306、8080-8083、8848、9848

3. **数据持久化**：MySQL数据和Nacos数据会持久化到Docker卷中，不会因容器重启丢失

4. **资源需求**：建议Docker Desktop分配至少4GB内存给Docker

5. **网络问题**：如果容器无法连接Nacos，检查Docker网络是否正常

### 常见问题

#### 1. 容器启动失败

```bash
# 查看详细日志
docker-compose logs [服务名]

# 检查端口占用
netstat -ano | findstr "3306"
```

#### 2. 编译失败

如果Maven编译失败，可能是内存不足：

```bash
# 增加Docker Desktop内存
# Docker Desktop -> Settings -> Resources -> Memory: 4GB+
```

#### 3. MySQL连接失败

```bash
# 检查MySQL容器状态
docker-compose ps mysql

# 查看MySQL日志
docker-compose logs mysql

# 重启MySQL
docker-compose restart mysql
```

#### 4. Nacos连接失败

```bash
# 检查Nacos是否健康
docker-compose ps nacos

# 查看Nacos日志
docker-compose logs nacos

# 等待Nacos完全启动（约30秒）
```

---

## 手动启动（非Docker方式）

## 启动顺序

### 1. 启动Nacos

首先启动Nacos服务注册和配置中心：

1. 下载Nacos Server：https://github.com/alibaba/nacos/releases
2. 解压后进入bin目录
3. Linux/Mac启动：`sh startup.sh -m standalone`
4. Windows启动：`cmd startup.cmd -m standalone`
5. 访问控制台：http://127.0.0.1:8848/nacos
6. 默认用户名/密码：nacos/nacos

### 2. 创建Nacos配置

在Nacos控制台创建以下配置文件：

#### 配置ID: erp-user-service-dev.yaml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_user?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root123456
```

#### 配置ID: erp-product-service-dev.yaml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_product?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root123456
```

#### 配置ID: erp-order-service-dev.yaml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_order?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root123456
```

### 3. 初始化数据库

执行数据库初始化脚本：

```bash
mysql -u root -p < database/erp-init.sql
```

### 4. 编译项目

在项目根目录执行Maven编译：

```bash
mvn clean install
```

### 5. 启动微服务

按照以下顺序启动微服务（每个服务在独立窗口中启动）：

#### 启动网关（必须先启动）
```bash
cd erp-gateway
mvn spring-boot:run
# 或运行GatewayApplication类
```

#### 启动用户服务
```bash
cd erp-user-service
mvn spring-boot:run
# 或运行UserServiceApplication类
```

#### 启动商品服务
```bash
cd erp-product-service
mvn spring-boot:run
# 或运行ProductServiceApplication类
```

#### 启动订单服务
```bash
cd erp-order-service
mvn spring-boot:run
# 或运行OrderServiceApplication类
```

## 服务访问地址

- **网关地址**: http://localhost:8080
- **用户服务API**: http://localhost:8080/user/...
- **商品服务API**: http://localhost:8080/product/...
- **订单服务API**: http://localhost:8080/order/...

## API文档

各服务的Swagger API文档：

- 用户服务：http://localhost:8081/swagger-ui/index.html
- 商品服务：http://localhost:8082/swagger-ui/index.html
- 订单服务：http://localhost:8083/swagger-ui/index.html

## 测试账号

- 用户名：admin
- 密码：123456

## 主要API接口

### 用户服务

#### 注册用户
```
POST /user/register
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456",
    "confirmPassword": "123456",
    "realName": "测试用户",
    "phone": "13800138000",
    "email": "test@erp.com"
}
```

#### 用户登录
```
POST /user/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

#### 分页查询用户
```
GET /user/page?page=1&size=10&username=admin&status=1
```

### 商品服务

#### 分页查询商品
```
GET /product/page?page=1&size=10&productName=笔记本&status=1
```

#### 添加商品
```
POST /product
Content-Type: application/json

{
    "productCode": "P006",
    "productName": "机械键盘",
    "categoryId": 1,
    "categoryName": "电子产品",
    "price": 399.00,
    "costPrice": 250.00,
    "stock": 200,
    "lowStock": 20,
    "status": 1
}
```

### 订单服务

#### 创建订单
```
POST /order
Content-Type: application/json

{
    "userId": 1,
    "receiverName": "张三",
    "receiverPhone": "13800138000",
    "receiverAddress": "北京市朝阳区xxx",
    "items": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 2,
            "quantity": 1
        }
    ]
}
```

#### 分页查询订单
```
GET /order/page?page=1&size=10&userId=1&orderStatus=0
```

## 注意事项

1. **Nacos版本**：确保使用Nacos 2.x版本，与Spring Cloud Alibaba 2021.0.5.0兼容
2. **数据库配置**：修改各服务的数据库连接配置为实际的数据库信息
3. **Maven依赖**：首次启动可能需要较长时间下载依赖
4. **端口占用**：确保8080-8083端口未被占用
5. **Lombok插件**：IDE需要安装Lombok插件才能正确编译

## 常见问题

### 1. Maven编译失败
- 检查Java版本是否为17
- 清理Maven缓存：mvn clean
- 更新依赖：mvn -U

### 2. 服务无法注册到Nacos
- 检查Nacos是否正常启动
- 检查网络连接
- 检查配置文件中的Nacos地址是否正确

### 3. 数据库连接失败
- 检查MySQL是否启动
- 检查数据库用户权限
- 检查数据库连接配置

## 开发建议

1. 使用IDEA作为IDE，安装Spring Tools插件
2. 安装Lombok插件
3. 使用Maven Helper插件管理依赖
4. 使用Spring Boot DevTools实现热部署
5. 使用Postman或Swagger测试API接口
