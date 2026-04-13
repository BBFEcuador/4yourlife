package com.foryourlife.clients.account.promises.infrastructure.persistence;

import com.foryourlife.clients.account.promises.domain.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JPAImplPromiseRepository extends JpaRepository<Promise, String>, JpaSpecificationExecutor<Promise> {
    List<Promise> findAllByTraining_Id(String trainingId);
    Optional<Promise> findFirstByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(String userId, LocalDate startDateIsLessThan, LocalDate endDateIsGreaterThan);

    Optional<Promise> findByUser_IdAndTraining_Name(String userId, String trainingName);
}
