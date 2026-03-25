package com.erp.order.entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 订单创建DTO
 * 
 * 用于接收前端提交的订单创建请求
 */
@Data
public class OrderCreateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 收货人姓名
     */
    @NotNull(message = "收货人姓名不能为空")
    private String receiverName;
    
    /**
     * 收货人电话
     */
    @NotNull(message = "收货人电话不能为空")
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    @NotNull(message = "收货地址不能为空")
    private String receiverAddress;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 订单明细列表
     */
    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemDTO> items;
    
    /**
     * 订单明细DTO
     */
    @Data
    public static class OrderItemDTO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 商品ID
         */
        @NotNull(message = "商品ID不能为空")
        private Long productId;
        
        /**
         * 购买数量
         */
        @NotNull(message = "购买数量不能为空")
        private Integer quantity;
    }
}
