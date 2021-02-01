package com.test.demoatomikos;

import java.lang.annotation.*;

/**
 * @author blackjack
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value();
}