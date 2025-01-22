package com.foryourlife.admin.auth.application;

import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {
    private AdminRepository repository;
    private Logger logger = LoggerFactory.getLogger(AdminLoginService.class);

    public AdminLoginService(AdminRepository repository) {
        this.repository = repository;
    }


}
