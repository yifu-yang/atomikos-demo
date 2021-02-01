package com.test.demoatomikos;

import com.test.demoatomikos.dao.Db1Mapper;
import com.test.demoatomikos.dao.Db2Mapper;
import com.test.demoatomikos.entity.Customer;
import com.test.demoatomikos.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author blackjack
 */
@Slf4j
@Service
public class SampleServiceImpl implements SampleService {
    private final Db1Mapper db1Mapper;
    private final Db2Mapper db2Mapper;

    public SampleServiceImpl(Db1Mapper db1Mapper, Db2Mapper db2Mapper) {
        this.db1Mapper = db1Mapper;
        this.db2Mapper = db2Mapper;
    }

    @Override
    @Transactional
    public Integer finish(String name) {
        Customer customer = new Customer();
        customer.setAge(1);
        customer.setName(name);
        Order order = new Order();
        order.setCode(1);
        order.setQuantity(100);
        db1Mapper.insertCustomer(customer);
        db2Mapper.insertOrder(order);
        return new Integer("1");
    }

    @Override
    @Transactional
    public Integer rollbackForException(String name) {
        Customer customer = new Customer();
        customer.setAge(1);
        customer.setName(name);
        Order order = new Order();
        order.setCode(1);
        order.setQuantity(100);
        db1Mapper.insertCustomer(customer);
        int a = 1 / 0;
        db2Mapper.insertOrder(order);
        return new Integer("1");
    }

    @Override
    @Transactional
    public Integer rollbackManually(String name) {
        Customer customer = new Customer();
        customer.setAge(1);
        customer.setName(name);
        Order order = new Order();
        order.setCode(1);
        order.setQuantity(100);
        db1Mapper.insertCustomer(customer);
        db2Mapper.insertOrder(order);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new Integer("1");
    }
}
