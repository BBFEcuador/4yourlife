package com.foryourlife.payments.plan.domain;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    void save(Plan plan);

    void saveAll(List<Plan> plans);

    List<Plan> getAll();

    Optional<Plan> findById(String id);
}
