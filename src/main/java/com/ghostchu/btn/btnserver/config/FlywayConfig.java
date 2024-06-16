package com.ghostchu.btn.btnserver.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class FlywayConfig {
    private static final String BASE_SQL_LOCATION = "db/migration/clickhouse";
    private static final String ENCODING = "UTF-8";
    @Value("${spring.flyway.enabled}")
    private boolean enableMigrate;

    @Autowired
    @Qualifier("clickHouseDataSource")
    private DataSource clickHouseDataSource;

    @Bean("for_execute_only_flyway_migrate")
    public Object migrate() {
        if(!enableMigrate)return null;
        Flyway flyway = Flyway.configure()
                .dataSource(clickHouseDataSource)
                .locations(BASE_SQL_LOCATION)
                .encoding(ENCODING)
                .baselineOnMigrate(true)
                .cleanDisabled(true)
                .table("base_flyway")
                .load();
        flyway.migrate();
        return new Object();
    }
}
