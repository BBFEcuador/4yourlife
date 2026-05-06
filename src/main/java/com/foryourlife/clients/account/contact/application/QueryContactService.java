package com.foryourlife.clients.account.contact.application;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryContactService {
    private final ContactRepository _contactRepository;

    public QueryContactService(ContactRepository _contactRepository) {
        this._contactRepository = _contactRepository;
    }

    public List<Contact> findAllByUser(String userId) {
        return _contactRepository.findAllByUser(userId);
    }

    public Optional<Contact> findById(String id) {
        return _contactRepository.findById(id);
    }
}
