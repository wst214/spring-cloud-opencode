package com.erp.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 
 * 设计说明：
 * 所有Controller接口都返回此类型的响应结果，确保前端可以统一处理
 * 包含状态码、消息、数据三个核心部分
 * 
 * @param <T> 响应数据的泛型类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应状态码
     * 
     * 约定：
     * - 200: 成功
     * - 400: 请求参数错误
     * - 401: 未认证（未登录）
     * - 403: 无权限访问
     * - 404: 资源不存在
     * - 500: 服务器内部错误
     */
    private Integer code;
    
    /**
     * 响应消息
     * 
     * 用于向调用方返回操作结果或错误信息
     * 成功时通常返回"操作成功"
     * 失败时返回具体的错误原因
     */
    private String message;
    
    /**
     * 响应数据
     * 
     * 泛型T可以是任意类型：
     * - 单个对象（如User、Product）
     * - 集合（如List<User>、List<Product>）
     * - 分页结果（PageResult）
     * - 也可以为null（当只需要返回状态和消息时）
     */
    private T data;
    
    /**
     * 响应时间戳
     * 
     * 记录服务器响应的时间，便于前端调试和问题排查
     * 使用毫秒级时间戳
     */
    private Long timestamp;
    
    /**
     * 私有构造函数
     * 使用静态工厂方法创建实例
     */
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应（无数据）
     * 
     * 适用场景：不需要返回具体数据的操作
     * 例如：删除操作、更新操作
     * 
     * @param <T> 泛型类型
     * @return Result实例，状态码200，消息"操作成功"
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    /**
     * 成功响应（带数据）
     * 
     * 适用场景：需要返回具体数据的查询操作
     * 例如：查询用户信息、查询商品列表
     * 
     * @param data 响应的数据
     * @param <T> 泛型类型
     * @return Result实例，状态码200，消息"操作成功"
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    /**
     * 成功响应（自定义消息）
     * 
     * 适用场景：需要明确说明操作结果的场景
     * 例如：批量操作"成功删除3条记录"
     * 
     * @param message 自定义成功消息
     * @param data 响应的数据
     * @param <T> 泛型类型
     * @return Result实例，状态码200，自定义消息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败响应（无数据）
     * 
     * 适用场景：操作失败但不需要返回具体数据
     * 
     * @param message 错误消息
     * @param <T> 泛型类型
     * @return Result实例，状态码500，错误消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    /**
     * 失败响应（自定义状态码）
     * 
     * 适用场景：需要区分不同类型错误的场景
     * 
     * @param code 自定义状态码
     * @param message 错误消息
     * @param <T> 泛型类型
     * @return Result实例，自定义状态码和错误消息
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 判断响应是否成功
     * 
     * 根据状态码是否为200判断操作是否成功
     * 
     * @return true表示成功，false表示失败
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
}
