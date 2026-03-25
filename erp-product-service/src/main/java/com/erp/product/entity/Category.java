package com.erp.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * 
 * 用于商品分类管理，支持多级分类
 */
@Data
@TableName("pms_category")
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 分类ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 父分类ID
     * 
     * 0表示顶级分类
     */
    private Long parentId;
    
    /**
     * 层级
     * 
     * 1：一级分类，2：二级分类，以此类推
     */
    private Integer level;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态
     * 
     * 0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
