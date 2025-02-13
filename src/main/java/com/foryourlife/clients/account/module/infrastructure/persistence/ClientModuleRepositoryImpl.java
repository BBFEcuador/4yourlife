package com.foryourlife.clients.account.module.infrastructure.persistence;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientModuleRepositoryImpl implements ClientModuleRepository {
    private final JPAModuleRepository repository;

    public ClientModuleRepositoryImpl(JPAModuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ClientModule clientModule) {
        this.repository.save(clientModule);
    }

    @Override
    public Optional<ClientModule> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<ClientModule> findByUserId(String userId) {
        return this.repository.findByUser_Id(userId);
    }
}
