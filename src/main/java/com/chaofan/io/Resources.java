package com.chaofan.io;

import java.io.InputStream;

/**
 * 加载配置文件工具类
 * @author Fairy
 * @date 2021/11/8
 * @since 1.0
 */
public class Resources {

    /**
     * 根据path加载配置文件
     * @param path 配置文件路径
     * @return InputStream
     */
    public static InputStream getResourceAsStream(String path){
        ClassLoader classLoader = Resources.class.getClassLoader();
        return classLoader.getResourceAsStream(path);
    }
}
