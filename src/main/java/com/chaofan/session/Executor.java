package com.chaofan.session;

import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/10
 */
public interface Executor {

    /**
     * 查找
     * @param configuration 核心配置类
     * @param mappedStatement 已封装的sql
     * @param params 参数
     * @param <E> 返回值类型
     * @return 结果
     */
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    /**
     * 更新
     * @param configuration 核心配置类
     * @param mappedStatement 已封装的sql
     * @param params 参数
     * @return 受影响的行数
     */
    int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    /**
     * 关闭连接
     */
    void close();
}
