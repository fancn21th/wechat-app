package org.checkin.datasource.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-16
 */
@Configuration
@ComponentScan(basePackages = {"org.checkin.datasource"})
@PropertySources({
        @PropertySource("classpath:/jdbc.properties"),
        @PropertySource("classpath:/c3p0.properties")
})
public class DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
        } catch (PropertyVetoException e) {
            LOGGER.error("Class load exception", e);
            throw new RuntimeException(e);
        }
        dataSource.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUser(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        dataSource.setAcquireIncrement(env.getRequiredProperty("c3p0.acquireIncrement", Integer.class));
        dataSource.setMinPoolSize(env.getRequiredProperty("c3p0.minPoolSize", Integer.class));
        dataSource.setMaxPoolSize(env.getRequiredProperty("c3p0.maxPoolSize", Integer.class));
        dataSource.setMaxIdleTime(env.getRequiredProperty("c3p0.maxIdleTime", Integer.class));
        dataSource.setTestConnectionOnCheckout(env.getRequiredProperty("c3p0.testConnectionOnCheckout", Boolean.class));
        return dataSource;
    }
}
