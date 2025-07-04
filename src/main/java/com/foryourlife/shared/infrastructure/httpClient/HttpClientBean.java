package com.foryourlife.shared.infrastructure.httpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientBean {

    @Bean
    public RestClient client(){
        return  RestClient.create();
    }
}
