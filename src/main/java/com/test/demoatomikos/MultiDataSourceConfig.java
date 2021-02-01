package com.test.demoatomikos;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author blackjack
 */
@Slf4j
@Configuration
public class MultiDataSourceConfig {

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String mysqlDriverClassName;
    @Value("${spring.datasource.poolConfig.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.poolConfig.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.poolConfig.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.poolConfig.maxWait}")
    private int maxWait;

    /**
     * db1 datasource configuration(MySQL)
     */
    @Value("${spring.datasource.db1.url}")
    private String db1Url;
    @Value("${spring.datasource.db1.username}")
    private String db1Username;
    @Value("${spring.datasource.db1.password}")
    private String db1Password;

    /**
     * db2 datasource configuration(MySQL)
     */
    @Value("${spring.datasource.db2.url}")
    private String db2Url;
    @Value("${spring.datasource.db2.username}")
    private String db2Username;
    @Value("${spring.datasource.db2.password}")
    private String db2Password;


    @Bean(name = "db2")
    public javax.sql.DataSource db2DataSource() {
        javax.sql.DataSource db2DataSource = createDataSource(db2Url, db2Username, db2Password, mysqlDriverClassName);
        log.info("init db2 complete ...... " + db2DataSource);
        return db2DataSource;
    }

    @Bean(name = "db1")
    public javax.sql.DataSource db1DataSource() {
        javax.sql.DataSource db1DataSource = createDataSource(db1Url, db1Username, db1Password, mysqlDriverClassName);
        log.info("init db1 complete ...... " + db1DataSource);
        return db1DataSource;
    }

    /**
     * create datasource
     *
     * @param url
     * @param username
     * @param password
     * @param driverClassName
     * @return
     */
    private DataSource createDataSource(String url, String username, String password, String driverClassName) {
        DruidXADataSource dataSource = new DruidXADataSource();
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        ds.setXaDataSource(dataSource);
        return ds;
    }

    /**
     * set dynamic datasource
     *
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource multipleDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(8);
        targetDataSources.put("db2", db2DataSource());
        targetDataSources.put("db1", db1DataSource());
        multipleDataSource.setTargetDataSources(targetDataSources);
        return multipleDataSource;
    }

    /**
     * avoid the dependency loop
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        sqlSessionFactoryBean.setTransactionFactory(new MultiDataSourceTransactionFactory());
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /**
     * use JtaTransactionManager for distributed transaction
     * @return
     */
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    public UserTransactionImp userTransactionImp() throws javax.transaction.SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(5000);
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager jtaTransactionManager() throws javax.transaction.SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }


    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setSlowSqlMillis(1000);
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }
}
