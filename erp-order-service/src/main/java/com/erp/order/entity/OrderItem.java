package com.erp.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细实体类
 * 
 * 存储订单中每个商品的具体信息
 */
@Data
@TableName("oms_order_item")
public class OrderItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 明细ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品编码
     */
    private String productCode;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productImage;
    
    /**
     * 商品单价
     */
    private BigDecimal price;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 小计金额
     */
    private BigDecimal totalAmount;
}
