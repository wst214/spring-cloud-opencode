package com.erp.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.erp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 * 
 * 继承说明：
 * - BaseEntity提供id、createTime、updateTime等公共字段
 * - 使用MyBatis Plus的注解配置数据库映射
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    
    /**
     * 逻辑删除标记
     * 0-未删除，1-已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    
    /**
     * 用户名
     * 
     * 用于用户登录，唯一标识
     * 最大长度：50个字符
     */
    private String username;
    
    /**
     * 密码
     * 
     * 用户登录密码
     * 存储时使用BCrypt加密
     * 最大长度：200个字符
     */
    private String password;
    
    /**
     * 真实姓名
     * 
     * 用户的真实姓名
     * 最大长度：50个字符
     */
    private String realName;
    
    /**
     * 手机号码
     * 
     * 用户的联系电话
     * 最大长度：20个字符
     */
    private String phone;
    
    /**
     * 邮箱
     * 
     * 用户的电子邮箱
     * 最大长度：100个字符
     */
    private String email;
    
    /**
     * 性别
     * 
     * 取值：0-女，1-男，2-未知
     * 使用Integer类型便于数据库存储
     */
    private Integer gender;
    
    /**
     * 状态
     * 
     * 取值：0-禁用，1-启用
     * 禁用的用户不能登录系统
     */
    private Integer status;
    
    /**
     * 部门ID
     * 
     * 关联用户所属的部门
     * 方便进行权限管理和组织架构管理
     */
    private Long deptId;
    
    /**
     * 头像URL
     * 
     * 存储用户头像的图片地址
     * 最大长度：255个字符
     */
    private String avatar;
}
