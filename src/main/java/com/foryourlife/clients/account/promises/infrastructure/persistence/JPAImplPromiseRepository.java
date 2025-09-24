package com.foryourlife.clients.account.promises.infrastructure.persistence;

import com.foryourlife.clients.account.promises.domain.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JPAImplPromiseRepository extends JpaRepository<Promise, String>, JpaSpecificationExecutor<Promise> {
    List<Promise> findAllByTraining_Id(String trainingId);

    Optional<Promise> findFirstByParticipant_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(String participantId, LocalDate startDateIsLessThan, LocalDate endDateIsGreaterThan);
}
