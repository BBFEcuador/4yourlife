package com.foryourlife.clients.account.contact.domain;

import java.util.List;

public interface ContactRepository {
    void save(Contact contact);
    void deleteById(String id);
    void update(Contact contact);
    List<Contact> findAllByUser(String userId);
    Contact findById(String id);
}
