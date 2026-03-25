package com.erp.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.order.entity.Order;
import com.erp.order.entity.OrderCreateDTO;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {
    
    /**
     * 创建订单
     */
    Order createOrder(OrderCreateDTO createDTO);
    
    /**
     * 分页查询订单列表
     */
    Page<Order> findPage(Integer page, Integer size, Long userId, Integer orderStatus);
    
    /**
     * 根据订单号查询订单
     */
    Order findByOrderNo(String orderNo);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);
    
    /**
     * 更新订单状态
     */
    void updateOrderStatus(Long orderId, Integer orderStatus);
}
