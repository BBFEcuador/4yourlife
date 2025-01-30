package com.foryourlife.clients.account.phone.domain;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository {
    void save(Phone phone);
    void deleteById(String id);
    List<Phone> findAllByUser(String userId);
    Optional<Phone> findById(String id);
}
