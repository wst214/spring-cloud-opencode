package com.erp.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.product.entity.Product;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {
    
    /**
     * 分页查询商品列表
     */
    Page<Product> findPage(Integer page, Integer size, String productName, Long categoryId, Integer status);
    
    /**
     * 根据编码查询商品
     */
    Product findByCode(String productCode);
    
    /**
     * 修改商品状态
     */
    void updateStatus(Long productId, Integer status);
    
    /**
     * 更新库存
     */
    void updateStock(Long productId, Integer quantity);
}
