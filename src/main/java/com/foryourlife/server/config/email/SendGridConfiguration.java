package com.foryourlife.server.config.email;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfiguration {
    private final SendGridConfigurationProperties properties;

    public SendGridConfiguration(SendGridConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(properties.getApiKey());
    }

    @Bean
    public Email fromEmail() {
        return new Email(properties.getFromEmail(), properties.getFromName());
    }
}