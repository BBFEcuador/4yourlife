package com.foryourlife.clients.account.contact.application;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import com.foryourlife.clients.account.contact.infrastructure.httpControllers.SaveContactRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandContactService {
    private final ContactRepository _contactRepository;
    private final ParticipantRepository _participantRepository;

    public CommandContactService(ContactRepository _contactRepository, ParticipantRepository _participantRepository) {
        this._contactRepository = _contactRepository;
        this._participantRepository = _participantRepository;
    }

    public void save(SaveContactRequest contactReq) {
        Contact contact = contactReq.toDomain();
        contact.setUser(
                _participantRepository.findById(contactReq.getUserId()).orElseThrow(
                        () -> new BaseException("Usuario no encontrado", List.of(
                                "No se encontro el usuario con id " + contactReq.getUserId()
                        ))
                )
        );
        _contactRepository.save(contact);
    }

    public void deleteById(String id) {
        _contactRepository.deleteById(id);
    }
}
