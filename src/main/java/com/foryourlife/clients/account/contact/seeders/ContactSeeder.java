package com.foryourlife.clients.account.contact.seeders;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.contact.domain.ContactRepository;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactSeeder {
    private final ContactRepository _contactRepository;
    private final UserRepository _userRepository;

    public ContactSeeder(ContactRepository _contactRepository, UserRepository userRepository) {
        this._contactRepository = _contactRepository;
        _userRepository = userRepository;
    }

    @Bean
    CommandLineRunner initContact() {
        return args -> {
            Users user = _userRepository.findById("84e0c1f5-d3e7-4a9f-83e3-83c0a40b9212").map(users -> Users.create(users.getId(), users.getEmail(), users.getPassword(), users.getName(), users.getPhone(), users.getParticipantLevel()))
                    .orElseThrow(() -> new RuntimeException("User error"));
            _contactRepository.save(Contact.create("f9d07722-bb8f-40c0-9ae7-738feaab62b9", "Catalina de Aldaz", "Mother", "0989999995", user));
        };
    }
}
