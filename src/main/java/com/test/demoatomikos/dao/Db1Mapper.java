package com.test.demoatomikos.dao;

import com.test.demoatomikos.entity.Customer;
import com.test.demoatomikos.DataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author blackjack
 */
@Mapper
public interface Db1Mapper {
    /**
     * insert into db1
     * @param customer
     * @return
     */
    @DataSource("db1")
    int insertCustomer(Customer customer);
}
