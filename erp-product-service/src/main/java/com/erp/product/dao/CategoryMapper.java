package com.erp.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
