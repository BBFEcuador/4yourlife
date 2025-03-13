package com.foryourlife.server.config;

import java.util.List;

public class UnauthRoutes {
    public static final List<String> ROUTES = List.of(
            "/api/metric/**",
            "/api/public/**",
            "/api/server/**",
            "/api/actuator/**",
            "/api/auth/**",
            "/api/admin/login",
            "/api/swagger-ui/**",
            "/api/swagger-ui.html/**",
            "/api/v3/api-docs/**"
    );
}
