package com.erp.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
