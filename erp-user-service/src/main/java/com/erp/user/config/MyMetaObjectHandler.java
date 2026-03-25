package com.erp.user.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus自动填充处理器
 * 
 * 功能说明：
 * - 自动填充创建时间、更新时间、创建人、更新人等字段
 * - 配合BaseEntity实体类的@TableField注解使用
 * - 无需在Service和Controller中手动设置这些字段
 * 
 * 使用场景：
 * - 插入数据时自动填充createTime、createBy
 * - 更新数据时自动填充updateTime、updateBy
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    /**
     * 插入操作时自动填充
     * 
     * 当执行insert操作时，自动填充以下字段：
     * - createTime：创建时间
     * - updateTime：更新时间
     * - createBy：创建人ID（暂时设为0，实际应用中应从Security Context获取）
     * - updateBy：更新人ID（暂时设为0）
     * 
     * @param metaObject MyBatis Plus的元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        
        // 填充创建人（暂时设为0，实际应用中应从登录用户信息获取）
        this.strictInsertFill(metaObject, "createBy", Long.class, 0L);
        // 填充更新人（暂时设为0）
        this.strictInsertFill(metaObject, "updateBy", Long.class, 0L);
    }
    
    /**
     * 更新操作时自动填充
     * 
     * 当执行update操作时，自动填充以下字段：
     * - updateTime：更新时间
     * - updateBy：更新人ID（暂时设为0，实际应用中应从Security Context获取）
     * 
     * @param metaObject MyBatis Plus的元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        // 填充更新人（暂时设为0，实际应用中应从登录用户信息获取）
        this.strictUpdateFill(metaObject, "updateBy", Long.class, 0L);
    }
}
