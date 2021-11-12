package com.chaofan.config;

import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/9
 */
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 把Mappers存到Configuration中
     * @param inputStream mappers流
     */
    @SuppressWarnings("unchecked")
    public void parse(InputStream inputStream) throws Exception {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectList = rootElement.selectNodes("//select");
        for (Element element : selectList) {
            //获取每个属性
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramType = element.attributeValue("paramType");
            String sqlText = element.getTextTrim();
            //封装到MappedStatement
            MappedStatement statement = new MappedStatement();
            statement.setId(id);
            statement.setResultType(resultType);
            statement.setParamType(paramType);
            statement.setSql(sqlText);
            String key = namespace + "." + id;
            //保存在map中
            configuration.getMappedStatementMap().put(key, statement);
        }
    }
}
