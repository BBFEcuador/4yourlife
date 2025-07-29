package com.foryourlife.server.config.email

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated


@Validated
@ConfigurationProperties(prefix = "com.email.sendgrid")
data class SendGridConfigurationProperties (
    @NotBlank
    @Pattern(regexp = "^SG[0-9a-zA-Z._]{67}$")
    val apiKey: String?,

    @Email
    @NotBlank
    val fromEmail: String?,

    @NotBlank
    val fromName: String?,
){

}