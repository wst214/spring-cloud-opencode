-- =============================================
-- ERP系统数据库初始化脚本
-- 数据库：MySQL 8.0+
-- =============================================

-- 创建用户数据库
CREATE DATABASE IF NOT EXISTS erp_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 创建商品数据库
CREATE DATABASE IF NOT EXISTS erp_product DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 创建订单数据库
CREATE DATABASE IF NOT EXISTS erp_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- =============================================
-- 用户服务数据库表
-- =============================================
USE erp_user;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    gender INT DEFAULT 2 COMMENT '性别：0-女，1-男，2-未知',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    dept_id BIGINT DEFAULT 0 COMMENT '部门ID',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人',
    update_by BIGINT DEFAULT 0 COMMENT '更新人',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 初始化测试用户（密码：123456）
INSERT INTO sys_user (id, username, password, real_name, phone, email, gender, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', '13800138000', 'admin@erp.com', 1, 1),
(2, 'user01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '测试用户', '13800138001', 'user01@erp.com', 1, 1);

-- =============================================
-- 商品服务数据库表
-- =============================================
USE erp_product;

-- 商品分类表
CREATE TABLE IF NOT EXISTS pms_category (
    id BIGINT NOT NULL COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level INT DEFAULT 1 COMMENT '层级',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    icon VARCHAR(255) DEFAULT NULL COMMENT '图标',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS pms_product (
    id BIGINT NOT NULL COMMENT '主键ID',
    product_code VARCHAR(50) NOT NULL COMMENT '商品编码',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID',
    category_name VARCHAR(100) DEFAULT NULL COMMENT '分类名称',
    image_url VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    description TEXT COMMENT '商品描述',
    spec_model VARCHAR(100) DEFAULT NULL COMMENT '规格型号',
    unit VARCHAR(20) DEFAULT '个' COMMENT '单位',
    price DECIMAL(10,2) NOT NULL COMMENT '销售价格',
    cost_price DECIMAL(10,2) DEFAULT NULL COMMENT '成本价格',
    stock INT DEFAULT 0 COMMENT '库存数量',
    low_stock INT DEFAULT 10 COMMENT '库存预警值',
    status INT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    supplier_id BIGINT DEFAULT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(100) DEFAULT NULL COMMENT '供应商名称',
    weight DECIMAL(10,2) DEFAULT NULL COMMENT '重量（克）',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人',
    update_by BIGINT DEFAULT 0 COMMENT '更新人',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted INT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 初始化测试分类
INSERT INTO pms_category (id, name, parent_id, level, sort, status) VALUES
(1, '电子产品', 0, 1, 1, 1),
(2, '办公用品', 0, 1, 2, 1),
(3, '服装', 0, 1, 3, 1);

-- 初始化测试商品
INSERT INTO pms_product (id, product_code, product_name, category_id, category_name, price, cost_price, stock, low_stock, status) VALUES
(1, 'P001', '笔记本电脑', 1, '电子产品', 5999.00, 4500.00, 100, 10, 1),
(2, 'P002', '无线鼠标', 1, '电子产品', 99.00, 50.00, 500, 50, 1),
(3, 'P003', 'A4打印纸', 2, '办公用品', 25.00, 15.00, 1000, 100, 1),
(4, 'P004', '签字笔', 2, '办公用品', 5.00, 2.00, 2000, 200, 1),
(5, 'P005', '男士T恤', 3, '服装', 199.00, 80.00, 300, 30, 1);

-- =============================================
-- 订单服务数据库表
-- =============================================
USE erp_order;

-- 订单表
CREATE TABLE IF NOT EXISTS oms_order (
    id BIGINT NOT NULL COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) DEFAULT NULL COMMENT '用户名称',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '应付金额',
    freight_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    order_status INT DEFAULT 0 COMMENT '订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消',
    pay_status INT DEFAULT 0 COMMENT '支付状态：0-未支付，1-已支付，2-已退款',
    pay_type INT DEFAULT NULL COMMENT '支付方式：1-微信，2-支付宝，3-银行卡',
    pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(255) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS oms_order_item (
    id BIGINT NOT NULL COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_code VARCHAR(50) DEFAULT NULL COMMENT '商品编码',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- =============================================
-- 查询所有创建的数据库
-- =============================================
SHOW DATABASES;
