package com.erp.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * 
 * 设计说明：
 * 用于封装分页查询的结果，包含总记录数、当前页数据列表等信息
 * 配合MyBatis Plus的IPage使用，可以方便地进行分页查询
 * 
 * @param <T> 分页数据的泛型类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 总记录数
     * 
     * 表示符合条件的总数据条数，不受分页影响
     * 前端可以根据此值计算总页数：total / pageSize
     */
    private Long total;
    
    /**
     * 当前页数据列表
     * 
     * 存储当前页的所有数据记录
     */
    private List<T> records;
    
    /**
     * 当前页码
     * 
     * 从1开始计数，不是从0开始
     */
    private Long current;
    
    /**
     * 每页显示条数
     * 
     * 每页返回的最大记录数
     */
    private Long size;
    
    /**
     * 总页数
     * 
     * 根据total和size计算得出
     * 计算公式：(total + size - 1) / size
     */
    private Long pages;
    
    /**
     * 构造分页结果
     * 
     * @param total 总记录数
     * @param records 当前页数据列表
     */
    public PageResult(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
    
    /**
     * 构造分页结果（包含分页信息）
     * 
     * @param total 总记录数
     * @param records 当前页数据列表
     * @param current 当前页码
     * @param size 每页条数
     */
    public PageResult(Long total, List<T> records, Long current, Long size) {
        this.total = total;
        this.records = records;
        this.current = current;
        this.size = size;
        // 计算总页数
        this.pages = (total + size - 1) / size;
    }
}
