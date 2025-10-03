package com.foryourlife.server.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
public class JsonConfig {
    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
    }
}
