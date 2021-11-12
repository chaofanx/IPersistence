package com.chaofan.session;

import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/9
 */
public interface SqlSession {

    /**
     * 查询多个
     * @param statementId sql的id
     * @param params 参数列表
     * @param <E> 返回类型
     * @return List<E>
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    /**
     * 按条件查询单个
     * @param statementId sql的id
     * @param params 参数列表
     * @param <T> 返回类型
     * @return T
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;

    <T> T selectOne(String statementId,Class<T> clazz, Object... params);

    /**
     * 更新内容
     * @param statementId sql的id
     * @param params 参数列表
     * @return 受影响的行数
     */
    int update(String statementId, Object params);

    /**
     * 保存对象
     * @param statementId
     * @param params
     * @return
     */
    int save(String statementId, Object params);

    int delete(String statementId, Object params);

}
