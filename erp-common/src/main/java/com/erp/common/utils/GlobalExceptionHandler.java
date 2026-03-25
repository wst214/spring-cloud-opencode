package com.erp.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * 设计说明：
 * 统一处理项目中抛出的所有异常，避免在每个Controller中单独处理
 * 根据异常类型返回统一的错误响应格式
 * 
 * 功能：
 * 1. 处理业务异常（BusinessException）
 * 2. 处理参数校验异常（MethodArgumentNotValidException、BindException）
 * 3. 处理运行时异常（RuntimeException）
 * 4. 处理其他未捕获的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     * 
     * @param e 业务异常实例
     * @param request HTTP请求对象
     * @return 统一响应结果
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        // 记录异常日志
        log.error("业务异常发生，路径：{}，错误信息：{}", request.getRequestURI(), e.getMessage());
        
        // 返回业务异常信息
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数校验异常（@RequestBody参数校验）
     * 
     * 当使用@Valid或@Validated注解进行参数校验失败时触发
     * 例如：@NotBlank、@NotNull、@Size等注解校验失败
     * 
     * @param e 参数校验异常实例
     * @param request HTTP请求对象
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        // 记录异常日志
        log.error("参数校验异常发生，路径：{}，错误信息：{}", request.getRequestURI(), e.getMessage());
        
        // 收集所有校验错误信息
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            // field：字段名，defaultMessage：默认错误消息
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        // 返回错误详情
        return Result.error(400, "参数校验失败：" + errors);
    }
    
    /**
     * 处理参数绑定异常（表单参数校验）
     * 
     * 当使用@Valid或@Validated注解进行表单参数校验失败时触发
     * 
     * @param e 参数绑定异常实例
     * @param request HTTP请求对象
     * @return 统一响应结果
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleBindException(
            BindException e, 
            HttpServletRequest request) {
        // 记录异常日志
        log.error("参数绑定异常发生，路径：{}，错误信息：{}", request.getRequestURI(), e.getMessage());
        
        // 收集所有绑定错误信息
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        // 返回错误详情
        return Result.error(400, "参数绑定失败：" + errors);
    }
    
    /**
     * 处理运行时异常
     * 
     * 捕获程序运行过程中未预料的异常
     * 
     * @param e 运行时异常实例
     * @param request HTTP请求对象
     * @return 统一响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        // 记录异常日志，包括堆栈信息
        log.error("运行时异常发生，路径：{}，错误信息：{}，堆栈：{}", 
                request.getRequestURI(), e.getMessage(), e.getStackTrace());
        
        // 返回通用错误消息，不暴露具体异常信息
        return Result.error("系统繁忙，请稍后再试");
    }
    
    /**
     * 处理所有未捕获的异常
     * 
     * 作为最后的异常处理防线，捕获所有上述处理器未能处理的异常
     * 
     * @param e 异常实例
     * @param request HTTP请求对象
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        // 记录异常日志
        log.error("未知异常发生，路径：{}，错误类型：{}，错误信息：{}", 
                request.getRequestURI(), e.getClass().getName(), e.getMessage());
        
        // 返回通用错误消息
        return Result.error("系统错误，请联系管理员");
    }
}
