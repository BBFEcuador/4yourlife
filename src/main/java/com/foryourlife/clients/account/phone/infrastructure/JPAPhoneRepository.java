package com.foryourlife.clients.account.phone.infrastructure;

import com.foryourlife.clients.account.phone.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPAPhoneRepository extends JpaRepository<Phone, String> {
    List<Phone> findByUser_id(String userId);
}
