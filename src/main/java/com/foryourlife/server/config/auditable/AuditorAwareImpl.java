package com.foryourlife.server.config.auditable;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String who = null;

            Object credentials = auth.getCredentials();
            if (credentials instanceof String cred && !cred.isBlank()) {
                who = cred;
            }

            if (who == null || who.isBlank()) {
                who = auth.getName();
            }

            return Optional.of(who != null ? who : "system");
        }

        return Optional.of("system");
    }
}