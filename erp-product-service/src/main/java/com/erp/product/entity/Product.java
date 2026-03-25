package com.erp.product.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体类
 * 
 * 包含商品的基本信息和库存管理
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_product")
public class Product extends BaseEntity {
    
    /**
     * 逻辑删除标记
     * 0-未删除，1-已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    
    /**
     * 商品编码
     * 
     * 商品的唯一标识符，便于系统内部检索
     * 最大长度：50个字符
     */
    private String productCode;
    
    /**
     * 商品名称
     * 
     * 商品的展示名称
     * 最大长度：100个字符
     */
    private String productName;
    
    /**
     * 商品分类ID
     * 
     * 关联商品分类表，用于分类管理
     */
    private Long categoryId;
    
    /**
     * 分类名称
     * 
     * 商品所属分类的名称（非数据库字段，便于展示）
     */
    private String categoryName;
    
    /**
     * 商品图片URL
     * 
     * 商品的主图地址
     * 最大长度：255个字符
     */
    private String imageUrl;
    
    /**
     * 商品描述
     * 
     * 商品的详细描述信息
     */
    private String description;
    
    /**
     * 规格型号
     * 
     * 商品的规格型号信息
     * 最大长度：100个字符
     */
    private String specModel;
    
    /**
     * 单位
     * 
     * 商品的计量单位，如：个、箱、件等
     * 最大长度：20个字符
     */
    private String unit;
    
    /**
     * 销售价格
     * 
     * 商品的销售单价
     * 使用BigDecimal保证精度
     */
    private BigDecimal price;
    
    /**
     * 成本价格
     * 
     * 商品的进货成本价
     * 用于利润计算
     */
    private BigDecimal costPrice;
    
    /**
     * 库存数量
     * 
     * 当前仓库中的商品库存数量
     */
    private Integer stock;
    
    /**
     * 库存预警值
     * 
     * 当库存低于此值时，提醒管理员补货
     */
    private Integer lowStock;
    
    /**
     * 状态
     * 
     * 取值：0-下架，1-上架
     */
    private Integer status;
    
    /**
     * 供应商ID
     * 
     * 商品的供应商
     */
    private Long supplierId;
    
    /**
     * 供应商名称
     */
    private String supplierName;
    
    /**
     * 重量
     * 
     * 商品的重量（克）
     */
    private BigDecimal weight;
    
    /**
     * 排序
     * 
     * 用于商品列表排序
     */
    private Integer sort;
}
