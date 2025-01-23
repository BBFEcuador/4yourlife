package com.foryourlife.clients.account.phone.domain;

import java.util.List;

public interface PhoneRepository {
    void save(Phone phone);
    void update(Phone phone);
    void deleteById(String id);
    List<Phone> findAllByUser(String userId);
}
