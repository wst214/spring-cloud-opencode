package com.erp.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.utils.BusinessException;
import com.erp.order.dao.OrderItemMapper;
import com.erp.order.dao.OrderMapper;
import com.erp.order.entity.Order;
import com.erp.order.entity.OrderCreateDTO;
import com.erp.order.entity.OrderItem;
import com.erp.order.feign.ProductFeignClient;
import com.erp.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductFeignClient productFeignClient;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderCreateDTO createDTO) {
        // 1. 生成订单编号
        String orderNo = generateOrderNo();
        
        // 2. 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(createDTO.getUserId());
        order.setReceiverName(createDTO.getReceiverName());
        order.setReceiverPhone(createDTO.getReceiverPhone());
        order.setReceiverAddress(createDTO.getReceiverAddress());
        order.setRemark(createDTO.getRemark());
        order.setOrderStatus(0); // 待付款
        order.setPayStatus(0); // 未支付
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 3. 处理订单明细
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderCreateDTO.OrderItemDTO itemDTO : createDTO.getItems()) {
            // 通过Feign调用商品服务获取商品信息
            Object productObj = productFeignClient.getById(itemDTO.getProductId());
            
            // 简化处理，实际应该解析productObj获取商品信息
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setProductCode("P" + itemDTO.getProductId());
            orderItem.setProductName("商品" + itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(100)); // 简化处理
            orderItem.setTotalAmount(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalAmount());
            
            // 扣减库存（通过Feign调用）
            try {
                productFeignClient.deductStock(itemDTO.getProductId(), -itemDTO.getQuantity());
            } catch (Exception e) {
                log.error("扣减库存失败，商品ID：{}，数量：{}", itemDTO.getProductId(), itemDTO.getQuantity(), e);
                throw new BusinessException("库存扣减失败");
            }
        }
        
        // 4. 设置订单金额
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        
        // 5. 保存订单
        boolean orderResult = this.save(order);
        if (!orderResult) {
            throw new BusinessException("创建订单失败");
        }
        
        // 6. 保存订单明细
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            orderItemMapper.insert(orderItem);
        }
        
        // 7. 设置订单明细到订单对象
        order.setItems(orderItems);
        
        log.info("创建订单成功，订单号：{}，用户ID：{}，金额：{}", orderNo, createDTO.getUserId(), totalAmount);
        return order;
    }
    
    @Override
    public Page<Order> findPage(Integer page, Integer size, Long userId, Integer orderStatus) {
        Page<Order> pageResult = new Page<>(page, size);
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (orderStatus != null) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        
        Page<Order> result = this.page(pageResult, wrapper);
        
        // 填充订单明细
        for (Order order : result.getRecords()) {
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            order.setItems(items);
        }
        
        return result;
    }
    
    @Override
    public Order findByOrderNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = this.getOne(wrapper);
        
        if (order != null) {
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            order.setItems(items);
        }
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getOrderStatus() != 0) {
            throw new BusinessException("只有待付款的订单才能取消");
        }
        
        // 取消订单
        order.setOrderStatus(4); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        boolean result = this.updateById(order);
        if (!result) {
            throw new BusinessException("取消订单失败");
        }
        
        // 恢复库存
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
        
        for (OrderItem item : items) {
            try {
                productFeignClient.deductStock(item.getProductId(), item.getQuantity());
            } catch (Exception e) {
                log.error("恢复库存失败，商品ID：{}，数量：{}", item.getProductId(), item.getQuantity(), e);
            }
        }
        
        log.info("取消订单成功，订单号：{}", order.getOrderNo());
    }
    
    @Override
    public void updateOrderStatus(Long orderId, Integer orderStatus) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        order.setOrderStatus(orderStatus);
        order.setUpdateTime(LocalDateTime.now());
        boolean result = this.updateById(order);
        if (!result) {
            throw new BusinessException("更新订单状态失败");
        }
        
        log.info("更新订单状态成功，订单号：{}，新状态：{}", order.getOrderNo(), orderStatus);
    }
    
    /**
     * 生成订单编号
     * 格式：ORD + 年月日时分秒 + 4位随机数
     */
    private String generateOrderNo() {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        int random = (int) (Math.random() * 10000);
        return "ORD" + timestamp + String.format("%04d", random);
    }
}
