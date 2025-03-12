package com.foryourlife.clients.account.module.application;

import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.module.domain.ClientModuleRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import org.springframework.stereotype.Service;

@Service
public class ClientModuleCreatorService {
    private final ClientModuleRepository repository;
    private final EventBus bus;

    public ClientModuleCreatorService(ClientModuleRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    public void createClientModule(ClientModule module){
        this.repository.save(module);
        bus.publish(module.pullDomainEvents());
    }

}
