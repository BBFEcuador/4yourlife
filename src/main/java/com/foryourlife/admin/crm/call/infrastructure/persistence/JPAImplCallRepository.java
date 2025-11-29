package com.foryourlife.admin.crm.call.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JPAImplCallRepository extends JpaRepository<Call, String>, JpaSpecificationExecutor<Call> {
    List<Call> findAllByTraining_Id(String trainingId);
}
