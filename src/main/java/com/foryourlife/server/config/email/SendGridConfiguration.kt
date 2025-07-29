package com.foryourlife.server.config.email

import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(SendGridConfigurationProperties::class)
open class SendGridConfiguration(private val sendGridConfigurationProperties: SendGridConfigurationProperties) {
    @Bean
    open fun sendGrid(): SendGrid {
        val apiKey: String = sendGridConfigurationProperties.apiKey!!
        return SendGrid(apiKey)
    }

    @Bean
    open fun fromEmail(): Email {
        val fromEmail: String? = sendGridConfigurationProperties.fromEmail
        val fromName: String? = sendGridConfigurationProperties.fromName
        return Email(fromEmail, fromName)
    }
}