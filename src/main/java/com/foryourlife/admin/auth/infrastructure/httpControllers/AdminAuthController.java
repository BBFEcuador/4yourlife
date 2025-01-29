package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.application.AdminCreateService;
import com.foryourlife.admin.auth.application.AdminFinderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AdminAuthController {
    @Autowired
    private AdminFinderService service;

    @Autowired
    private AdminCreateService serviceCreateService;

    @PostMapping("/admin-login")
    public ResponseEntity<?> login(@Valid  @RequestBody AdminLoginRequest request) {
        return new ResponseEntity<>(service.login(request.getUsername(), request.getPassword()),HttpStatus.OK);
    }
}
