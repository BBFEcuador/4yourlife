package com.foryourlife.admin.auth.application;

import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleFinderService {
    private final AdminRoleRepository repository;

    public AdminRoleFinderService(AdminRoleRepository repository) {
        this.repository = repository;
    }
}
