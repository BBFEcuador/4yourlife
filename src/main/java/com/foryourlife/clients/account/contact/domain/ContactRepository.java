package com.foryourlife.clients.account.contact.domain;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    void save(Contact contact);
    void deleteById(String id);
    List<Contact> findAllByUser(String userId);
    Optional<Contact> findById(String id);
}
