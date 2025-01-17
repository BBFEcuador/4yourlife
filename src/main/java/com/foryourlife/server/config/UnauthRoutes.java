package com.foryourlife.server.config;

import java.util.List;

public class UnauthRoutes {
    public static final List<String> ROUTES = List.of(
            "/metric/**",
            "/server/**",
            "/actuator/**",
            "/auth/**",
            "/admin/login",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/v3/api-docs/**"
    );
}
