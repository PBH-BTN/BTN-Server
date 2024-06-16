package com.ghostchu.btn.btnserver.config;

import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UnirestInstance unirest() {
        UnirestInstance instance = Unirest.spawnInstance();
        instance.config()
                .addDefaultHeader("User-Agent", "BTN-Server/1.0")
                .enableCookieManagement(true);
        return instance;
    }
}
