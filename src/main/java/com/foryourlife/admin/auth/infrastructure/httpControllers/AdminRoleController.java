package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/admin-role")
public class AdminRoleController {

    @Autowired
    private AdminRoleRepository repository;

    @GetMapping("")
    public ResponseEntity<?> getAllAdminRoles() {
        return new ResponseEntity<>(repository.getAll(),HttpStatus.OK);
    }
}
