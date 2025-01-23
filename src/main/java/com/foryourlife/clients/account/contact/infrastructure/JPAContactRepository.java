package com.foryourlife.clients.account.contact.infrastructure;

import com.foryourlife.clients.account.contact.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findByUser_id(String userId);
}
