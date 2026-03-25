package com.erp.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * 继承说明：
 * - BaseMapper<User> 是MyBatis Plus提供的通用Mapper接口
 * - 包含了常用的CRUD方法，无需手动编写SQL语句
 * - MyBatis Plus会根据实体类自动生成SQL
 * 
 * 功能：
 * - 提供用户数据的增删改查操作
 * - 支持条件查询、分页查询等高级功能
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * MyBatis Plus会自动提供以下常用方法：
     * 
     * 1. 插入操作
     * - int insert(User user) - 插入一条用户记录
     * 
     * 2. 删除操作
     * - int deleteById(Serializable id) - 根据ID删除
     * - int delete(Map<String, Object> map) - 根据条件删除
     * - int deleteBatchIds(Collection<?> list) - 批量删除
     * 
     * 3. 更新操作
     * - int updateById(User user) - 根据ID更新
     * - int update(User user, Wrapper<User> updateWrapper) - 根据条件更新
     * 
     * 4. 查询操作
     * - User selectById(Serializable id) - 根据ID查询
     * - List<User> selectBatchIds(Collection<?> list) - 批量查询
     * - List<User> selectByMap(Map<String, Object> map) - 根据条件查询
     * - List<User> selectList(Wrapper<User> queryWrapper) - 条件查询列表
     * - Integer selectCount(Wrapper<User> queryWrapper) - 条件统计
     * - User selectOne(Wrapper<User> queryWrapper) - 查询一条记录
     * 
     * 如果需要自定义复杂SQL，建议在同目录的UserMapper.xml文件中编写
     */
}
