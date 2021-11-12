package com.chaofan.session;

/**
 * @author Fairy
 * @date 2021/11/8
 */
public interface SqlSessionFactory {

    /**
     * 生产一个SqlSession
     * @return SqlSession
     */
    SqlSession openSession();
}
