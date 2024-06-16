package com.ghostchu.btn.btnserver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JDBCTemplateConfig {
    @Bean("jdbcTemplateMySQL")
    @Primary
    public JdbcTemplate jdbcTemplateMySQL(@Qualifier("mysqlDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("jdbcTemplateClickHouse")
    public JdbcTemplate jdbcTemplateClickHouse(@Qualifier("clickHouseDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
