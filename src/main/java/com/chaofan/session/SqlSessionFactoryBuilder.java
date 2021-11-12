package com.chaofan.session;

import com.chaofan.config.XMLConfigBuilder;
import com.chaofan.pojo.Configuration;

import java.io.InputStream;

/**
 * @author Fairy
 * @date 2021/11/8
 */
public class SqlSessionFactoryBuilder {

    /**
     * 构建SqlSessionFactory
     * @param in 主配置文件流
     * @return DefaultSqlSessionFactory
     */
    public SqlSessionFactory build(InputStream in) throws Exception {
        //使用dom4j解析配置文件，封装到Configuration
        XMLConfigBuilder builder = new XMLConfigBuilder();
        Configuration configuration = builder.parseConfig(in);
        //创建SqlSessionFactory对象：生产SqlSession
        return new DefaultSqlSessionFactory(configuration);
    }
}
