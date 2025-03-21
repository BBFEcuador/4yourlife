package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Participant, String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUser_email(String email);
    Participant findByUser_Id(String userId);
}
