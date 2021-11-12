package com.chaofan.session;

import com.chaofan.config.BoundSql;
import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;
import com.chaofan.utils.GenericTokenParser;
import com.chaofan.utils.ParameterMapping;
import com.chaofan.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**L
 * @author Fairy
 * @date 2021/11/10
 */
public class SimpleExecutor implements Executor {
    @Override
    @SuppressWarnings("unchecked")
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        //获取sql
        String sql = mappedStatement.getSql();
        //转换sql
        BoundSql boundSql = getBuondSql(sql);
        //获取预处理对象
        PreparedStatement statement = connection.prepareStatement(boundSql.getSqlText());
        //设置参数
        String paramType = mappedStatement.getParamType();
        Class<?> type = getClassType(paramType);
        List<ParameterMapping> mappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < mappingList.size(); i++) {
            ParameterMapping parameterMapping = mappingList.get(i);
            String content = parameterMapping.getContent();
            Field field = type.getDeclaredField(content);
            //开启暴力访问
            field.setAccessible(true);
            E o = (E)field.get(params[0]);
            //i从0开始，所以加一
            statement.setObject(i+1, o);
        }
        //执行sql
        ResultSet resultSet = statement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        Object o = resultTypeClass.getDeclaredConstructor().newInstance();
        //封装结果集
        List<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                //获取字段名
                String columnName = metaData.getColumnName(i+1);
                Object value = resultSet.getObject(columnName);
                //根据表和实体关系，完成封装，内省
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }

    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
        if (paramType != null) {
            return Class.forName(paramType);
        }
        return null;
    }

    /**
     * 把 #{} 转换为 ?
     * 解析出#{}的值
     * @param sql 待处理的sql
     * @return 封装后的BoundSql
     */
    private BoundSql getBuondSql(String sql) {
        //标记处理类
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //解析出来的参数名
        List<ParameterMapping> mappings = tokenHandler.getParameterMappings();
        return new BoundSql(parseSql, mappings);
    }
}
