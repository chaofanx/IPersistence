package com.chaofan.config;

import com.chaofan.io.Resources;
import com.chaofan.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * @author Fairy
 * @date 2021/11/8
 */
public class XMLConfigBuilder {

    private final Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 解析配置文件，封装Configuration
     * 使用dom4j
     * @param in InputStream
     * @return Configuration
     */
    @SuppressWarnings("unchecked")
    public Configuration parseConfig(InputStream in) throws Exception {
        Document document = new SAXReader().read(in);
        //<configuration>
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setJdbcUrl(properties.getProperty("url"));
//        dataSource.setDriverClass(properties.getProperty("driver-class"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(dataSource);

        //解析mapper.xml
        //拿到路径
        List<Element> mapperList = rootElement.selectNodes("//mappers");
        //获得流并解析
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream mapperStream = Resources.getResourceAsStream(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(mapperStream);
        }
        return configuration;
    }
}
