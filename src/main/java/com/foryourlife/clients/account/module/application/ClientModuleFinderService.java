package com.foryourlife.clients.account.module.application;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientModuleFinderService {
    private final ClientModuleRepository repository;

    public ClientModuleFinderService(ClientModuleRepository repository) {
        this.repository = repository;
    }

    public ClientModule findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public ClientModule findByUserId(String userId) {
        return repository.findByUserId(userId).orElse(null);
    }
}
