package com.test.demoatomikos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author blackjack
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * use ThreadLocal
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>();

    public static void setDataSource(String dataSourceKey) {
        log.info("set datasource : {}", dataSourceKey);
        DATA_SOURCE_KEY.set(dataSourceKey);
    }

    public static String getDataSource() {
        log.info("get datasource : {}", DATA_SOURCE_KEY.get());
        return DATA_SOURCE_KEY.get();
    }

    /**
     * release resource
     */
    public static void clearDataSource() {
        DATA_SOURCE_KEY.remove();
    }

    /**
     * use a map store the connections
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String key = DATA_SOURCE_KEY.get();
        log.info("current data-source is {}", key);
        return key;
    }
}