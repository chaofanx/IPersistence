package com.chaofan.session;

import com.chaofan.pojo.Configuration;
import com.chaofan.pojo.MappedStatement;

import java.util.List;

/**
 * @author Fairy
 * @date 2021/11/10
 */
public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
