package com.erp.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.utils.PageResult;
import com.erp.common.utils.Result;
import com.erp.order.entity.Order;
import com.erp.order.entity.OrderCreateDTO;
import com.erp.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 订单管理控制器
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "提供订单创建、查询、状态管理等功能")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    @Operation(summary = "创建订单", description = "提交订单创建请求")
    public Result<Order> createOrder(@Validated @RequestBody OrderCreateDTO createDTO) {
        Order order = orderService.createOrder(createDTO);
        return Result.success("订单创建成功", order);
    }
    
    /**
     * 分页查询订单列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询订单", description = "支持多条件分页查询")
    public Result<PageResult<Order>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer orderStatus) {
        Page<Order> pageResult = orderService.findPage(page, size, userId, orderStatus);
        
        PageResult<Order> result = new PageResult<>(
                pageResult.getTotal(),
                pageResult.getRecords(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
        
        return Result.success(result);
    }
    
    /**
     * 根据订单号查询订单
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "查询订单", description = "根据订单号查询订单详情")
    public Result<Order> getByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.findByOrderNo(orderNo);
        return Result.success(order);
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消订单", description = "取消待付款的订单")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success("订单取消成功", null);
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "更新订单状态", description = "修改订单状态")
    public Result<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Integer orderStatus) {
        orderService.updateOrderStatus(id, orderStatus);
        return Result.success("订单状态更新成功", null);
    }
    
    /**
     * 获取所有订单
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有订单", description = "查询所有订单列表")
    public Result<List<Order>> getAll() {
        Page<Order> page = orderService.findPage(1, 100, null, null);
        return Result.success(page.getRecords());
    }
}
