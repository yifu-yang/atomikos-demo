package com.test.demoatomikos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author blackjack
 */
@Configuration
@MapperScan("com.test.demoatomikos.dao")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@SpringBootApplication
public class DemoAtomikosApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoAtomikosApplication.class, args);
    }
}
