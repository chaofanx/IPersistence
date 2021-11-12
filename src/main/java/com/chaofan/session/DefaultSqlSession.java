package com.chaofan.session;

import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/9
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        //调动simpleExecutor的query方法
        executor = new SimpleExecutor();
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
    public int update(String statementId, Object params) throws Exception {
        executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public int save(String statementId, Object params) throws Exception {
        executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public int delete(String statementId, Object params) throws Exception {
        executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperClass) {
        //jdk动态代理
        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, (proxy, method, args) -> {
            String methodName = method.getName();
            String className = method.getDeclaringClass().getName();
            String statementId = className + "." + methodName;
            //获取返回值类型
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof ParameterizedType) {
                return selectList(statementId, args);
            }
            return selectOne(statementId, args);
        });
        return (T) o;
    }

    @Override
    public void close() {
        try {
            executor.close();
        }catch(Exception ignore){}
    }

}
