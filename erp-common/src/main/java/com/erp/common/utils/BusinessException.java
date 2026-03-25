package com.erp.common.utils;

import lombok.Data;

/**
 * 业务异常类
 * 
 * 设计说明：
 * 用于处理业务逻辑中的异常情况
 * 与系统异常（500）不同，业务异常通常是用户操作不当导致的
 * 例如：用户不存在、余额不足、库存不足等
 * 
 * 使用方式：
 * throw new BusinessException("用户不存在");
 * throw new BusinessException(400, "参数错误");
 */
@Data
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     * 
     * 用于区分不同类型的业务异常
     * 遵循HTTP状态码约定
     */
    private Integer code;
    
    /**
     * 错误消息
     * 
     * 向用户展示的错误信息，应该简洁明了
     */
    private String message;
    
    /**
     * 构造方法（只传入消息）
     * 
     * 默认错误码为400（请求参数错误）
     * 
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
        this.message = message;
    }
    
    /**
     * 构造方法（传入错误码和消息）
     * 
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 构造方法（传入消息和原因）
     * 
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 400;
        this.message = message;
    }
}
