# =============================================
# ERP商品服务Dockerfile
# 多阶段构建
# =============================================

# =============================================
# 第一阶段：构建阶段
# =============================================
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /build

# 复制所有模块
COPY pom.xml .
COPY erp-common/pom.xml erp-common/
COPY erp-common/src erp-common/src
COPY erp-product-service/pom.xml erp-product-service/
COPY erp-product-service/src erp-product-service/src

# 1. 先安装父POM到本地仓库
WORKDIR /build
RUN mvn install -N -DskipTests -B

# 2. 再安装erp-common模块到本地仓库
WORKDIR /build/erp-common
RUN mvn clean install -DskipTests -B

# 3. 最后构建erp-product-service
WORKDIR /build/erp-product-service
RUN mvn clean package -DskipTests -B

# =============================================
# 第二阶段：运行阶段
# =============================================
FROM eclipse-temurin:17-jre-jammy

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# 复制JAR文件
COPY --from=builder /build/erp-product-service/target/*.jar app.jar

# 环境变量配置
ENV NACOS_SERVER_ADDR=nacos
ENV NACOS_SERVER_PORT=8848

# 暴露端口
EXPOSE 8082

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
