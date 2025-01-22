package com.foryourlife.admin.auth.application;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import com.foryourlife.admin.auth.domain.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminFinderService {
    private AdminRepository repository;
    private Logger logger = LoggerFactory.getLogger(AdminFinderService.class);

    public AdminFinderService(AdminRepository repository) {
        this.repository = repository;
    }

    public Admin findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Admin findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public AdminLoginResponse login(String username, String password){
        try{
            return this.repository.login(username, password);
        }catch (Exception e){
            logger.error("Error during admin login: {}", e.getMessage());
            throw e;
        }
    }
}
