package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Participant,String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByEmail (String email);
}
