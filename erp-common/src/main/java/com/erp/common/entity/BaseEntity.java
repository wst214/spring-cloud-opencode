package com.erp.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类基类
 * 
 * 提供所有实体类公共的字段和属性：
 * - id: 主键ID，使用雪花算法生成
 * - createTime: 创建时间
 * - updateTime: 更新时间
 * - createBy: 创建人ID
 * - updateBy: 更新人ID
 * - remark: 备注
 * 
 * 使用Lombok的@Data注解自动生成getter/setter方法
 * 实现Serializable接口支持序列化传输
 */
@Data
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     * 
     * 使用MyBatis Plus的雪花算法自动生成
     * 确保分布式环境下ID的唯一性
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    /**
     * 创建时间
     * 
     * 记录数据首次创建的时间
     * 自动填充，无需手动设置
     * 格式化为：yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * 
     * 记录数据最后修改的时间
     * 每次修改数据时自动更新
     * 格式化为：yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID
     * 
     * 记录创建该条数据操作用户的ID
     * 用于数据审计和追溯
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    /**
     * 更新人ID
     * 
     * 记录最后更新该条数据操作用户的ID
     * 用于数据审计和追溯
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    
    /**
     * 备注信息
     * 
     * 存储对该条数据的额外说明或注释
     */
    private String remark;
}
