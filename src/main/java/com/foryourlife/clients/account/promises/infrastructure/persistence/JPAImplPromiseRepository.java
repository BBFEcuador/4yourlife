package com.foryourlife.clients.account.promises.infrastructure.persistence;

import com.foryourlife.clients.account.promises.domain.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JPAImplPromiseRepository extends JpaRepository<Promise, String>, JpaSpecificationExecutor<Promise> {
    List<Promise> findAllByTraining_Id(String trainingId);
}
