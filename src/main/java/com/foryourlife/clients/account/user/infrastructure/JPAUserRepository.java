package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Participant,String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUser_email (String email);

    Participant findByUser_Id(String userId);

    String user(User user);
}
