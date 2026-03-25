package com.erp.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 
 * 包含订单的基本信息和关联的订单明细
 */
@Data
@TableName("oms_order")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 订单编号
     * 
     * 订单的唯一标识，格式：ORD + 时间戳 + 随机数
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 应付金额
     */
    private BigDecimal payAmount;
    
    /**
     * 运费金额
     */
    private BigDecimal freightAmount;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 订单状态
     * 
     * 0-待付款，1-待发货，2-待收货，3-已完成，4-已取消
     */
    private Integer orderStatus;
    
    /**
     * 支付状态
     * 
     * 0-未支付，1-已支付，2-已退款
     */
    private Integer payStatus;
    
    /**
     * 支付方式
     * 
     * 1-微信支付，2-支付宝，3-银行卡
     */
    private Integer payType;
    
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 订单明细列表
     * 
     * 非数据库字段，用于存储订单关联的商品明细
     */
    @TableField(exist = false)
    private List<OrderItem> items;
}
