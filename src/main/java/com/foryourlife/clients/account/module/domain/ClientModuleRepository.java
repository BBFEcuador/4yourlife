package com.foryourlife.clients.account.module.domain;

import java.util.Optional;

public interface ClientModuleRepository {
    void save(ClientModule clientModule);

    Optional<ClientModule> findById(String id);

    Optional<ClientModule> findByUserId(String userId);
}
