package com.foryourlife.admin.auth.application;


import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.infrastructure.httpControllers.CreateAdminRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminCreateService {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(AdminCreateService.class);

    public AdminCreateService(AdminRepository repository, PasswordEncoder passwordEncoder, EventBus bus) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.bus = bus;
    }

    public void create(CreateAdminRequest admin){
        var password = RandomStringUtils.randomAlphabetic(10);;
        var newAdmin =  Admin.create(admin.id != null ? admin.id : UUID.randomUUID().toString(), admin.name, admin.email, passwordEncoder.encode(password), admin.role, admin.campus,password);
        this.repository.save(newAdmin);
    }

}
