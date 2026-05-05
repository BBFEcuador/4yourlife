package com.foryourlife.admin.auth.application;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminLoginResponse;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminFinderService {
    private AdminRepository repository;
    private Logger logger = LoggerFactory.getLogger(AdminFinderService.class);

    public AdminFinderService(AdminRepository repository) {
        this.repository = repository;
    }

    public Admin findById(String id) {
        return repository.findById(id).orElseThrow(() -> new BaseException("Not found", List.of("Admin not found")));
    }

    public Admin findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public Page<Admin> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(pageable, criteria);
    }

    public Page<Admin> findAll(Pageable pageable) {
        return repository.findAll(pageable);
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
