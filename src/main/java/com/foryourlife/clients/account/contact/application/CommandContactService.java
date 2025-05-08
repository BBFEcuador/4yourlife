package com.foryourlife.clients.account.contact.application;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import com.foryourlife.clients.account.contact.infrastructure.httpControllers.SaveContactRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.clients.account.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommandContactService {
    private final ContactRepository _contactRepository;
    private final UserRepository _userRepository;

    public CommandContactService(ContactRepository _contactRepository, UserRepository _userRepository) {
        this._contactRepository = _contactRepository;
        this._userRepository = _userRepository;
    }

    public void save(SaveContactRequest contactReq) {
        Contact contact = contactReq.toDomain();
        contact.setUser(_userRepository.findById(contactReq.getUserId()).orElseThrow(() -> new BaseException("Usuario no encontrado", new ArrayList<>())));
        _contactRepository.save(contact);
    }

    public void deleteById(String id) {
        _contactRepository.deleteById(id);
    }
}
