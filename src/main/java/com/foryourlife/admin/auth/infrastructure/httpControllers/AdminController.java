package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.application.AdminCreateService;
import com.foryourlife.admin.auth.application.AdminFinderService;
import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminFinderService service;

    @Autowired
    private AdminCreateService serviceCreateService;

    @PostMapping("login")
    public AdminLoginResponse login(AdminLoginRequest request) {
        return service.login(request.getEmail(), request.getPassword());
    }
}
