package com.erp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.utils.BusinessException;
import com.erp.product.dao.ProductMapper;
import com.erp.product.entity.Product;
import com.erp.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Override
    public Page<Product> findPage(Integer page, Integer size, String productName, Long categoryId, Integer status) {
        Page<Product> pageResult = new Page<>(page, size);
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(productName)) {
            wrapper.like(Product::getProductName, productName);
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        
        return this.page(pageResult, wrapper);
    }
    
    @Override
    public Product findByCode(String productCode) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getProductCode, productCode);
        return this.getOne(wrapper);
    }
    
    @Override
    public void updateStatus(Long productId, Integer status) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        product.setStatus(status);
        boolean result = this.updateById(product);
        if (!result) {
            throw new BusinessException("更新商品状态失败");
        }
        
        log.info("更新商品状态成功，商品ID：{}，新状态：{}", productId, status);
    }
    
    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 计算新库存
        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new BusinessException("库存不足，当前库存：" + product.getStock() + "，需要：" + Math.abs(quantity));
        }
        
        product.setStock(newStock);
        boolean result = this.updateById(product);
        if (!result) {
            throw new BusinessException("更新库存失败");
        }
        
        log.info("更新商品库存成功，商品ID：{}，变更数量：{}，新库存：{}", productId, quantity, newStock);
    }
}
