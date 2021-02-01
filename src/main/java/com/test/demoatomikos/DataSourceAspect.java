package com.test.demoatomikos;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author blackjack
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution ( * com.test.demoatomikos.dao..*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void  beforeSwitchDataSource(JoinPoint point){
        DataSource dataSource = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(DataSource.class);
        if (dataSource != null) {
            log.info("datasource: "+dataSource.value());
            DynamicDataSource.setDataSource(dataSource.value());
        } else {
            log.info(point.getSignature() + "not found DataSource annotation,datasource will use defaultTargetDataSource.");
        }
    }

    @After("pointcut()")
    public void  afterSwitchDataSource(JoinPoint point){
        DynamicDataSource.clearDataSource();
    }
}