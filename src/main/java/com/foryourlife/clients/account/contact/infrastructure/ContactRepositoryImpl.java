package com.foryourlife.clients.account.contact.infrastructure;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactRepositoryImpl implements ContactRepository {

    private final JPAContactRepository _jpaContactRepository;

    public ContactRepositoryImpl(JPAContactRepository _jpaContactRepository) {
        this._jpaContactRepository = _jpaContactRepository;
    }

    @Override
    public void save(Contact contact) {
        _jpaContactRepository.save(contact);
    }

    @Override
    public void deleteById(String id) {
        _jpaContactRepository.deleteById(id);
    }

    @Override
    public List<Contact> findAllByUser(String userId) {
        return _jpaContactRepository.findByUser_id(userId);
    }

    @Override
    public Optional<Contact> findById(String id) {
        return _jpaContactRepository.findById(id);
    }
}
