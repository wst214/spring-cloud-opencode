package com.erp.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务Feign客户端
 * 
 * 使用Feign进行服务间通信
 * 通过@FeignClient指定要调用的服务名称
 */
@FeignClient(name = "erp-product-service", path = "/product")
public interface ProductFeignClient {
    
    /**
     * 根据商品ID查询商品信息
     * 
     * @param id 商品ID
     * @return 商品信息（JSON格式）
     */
    @GetMapping("/{id}")
    Object getById(@PathVariable("id") Long id);
    
    /**
     * 扣减库存
     * 
     * 订单创建时调用，扣减商品的库存数量
     * 
     * @param productId 商品ID
     * @param quantity 扣减数量（负数表示加库存）
     */
    @PutMapping("/stock/deduct")
    void deductStock(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity);
}
