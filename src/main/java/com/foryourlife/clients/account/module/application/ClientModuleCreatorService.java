package com.foryourlife.clients.account.module.application;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientModuleCreatorService {
    private final ClientModuleRepository repository;

    public ClientModuleCreatorService(ClientModuleRepository repository) {
        this.repository = repository;
    }

    public void createClientModule(ClientModule module){
        this.repository.save(module);
    }

}
