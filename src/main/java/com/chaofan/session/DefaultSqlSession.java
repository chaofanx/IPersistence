package com.chaofan.session;

import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;

import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/9
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        //调动simpleExecutor的query方法
        SimpleExecutor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> t = selectList(statementId, params);
        if (t.size() == 1) {
            return t.get(0);
        } else if (t.size() > 1){
            throw new RuntimeException("返回结果过多");
        }
        throw new RuntimeException("返回结果为空");
    }

    @Override
    public <T> T selectOne(String statementId, Class<T> clazz, Object... params) {
        return null;
    }

    @Override
    public int update(String statementId, Object params) {
        return 0;
    }

    @Override
    public int save(String statementId, Object params) {
        return 0;
    }

    @Override
    public int delete(String statementId, Object params) {
        return 0;
    }

}
