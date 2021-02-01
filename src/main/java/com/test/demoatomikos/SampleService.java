package com.test.demoatomikos;

/**
 * @author blackjack
 */
public interface SampleService {
    /**
     * finish transaction
     * @param name
     * @return
     */
    Integer finish(String name);

    /**
     * transaction rollback for exception
     * @param name
     * @return
     */
    Integer rollbackForException(String name);

    /**
     * transaction rollback manually
     * @param name
     * @return
     */
    Integer rollbackManually(String name);
}
