package com.register.DB;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.micronaut.context.annotation.ConfigurationProperties;

import javax.sql.DataSource;
import jakarta.inject.Singleton;

@Singleton
@ConfigurationProperties("datasources.default")
public class DBOConnection {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    @Singleton
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Singleton
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
