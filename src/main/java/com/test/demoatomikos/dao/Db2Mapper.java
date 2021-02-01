package com.test.demoatomikos.dao;

import com.test.demoatomikos.DataSource;
import com.test.demoatomikos.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author blackjack
 */
@Mapper
public interface Db2Mapper {
    /**
     * insert into db2
     * @param order
     * @return
     */
    @DataSource("db2")
    int insertOrder(Order order);
}
