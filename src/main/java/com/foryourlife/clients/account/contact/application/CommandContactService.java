package com.foryourlife.clients.account.contact.application;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class CommandContactService {
    private final ContactRepository _contactRepository;

    public CommandContactService(ContactRepository _contactRepository) {
        this._contactRepository = _contactRepository;
    }

    public void save(Contact contact) {
        _contactRepository.save(contact);
    }

    public void deleteById(String id) {
        _contactRepository.deleteById(id);
    }
}
