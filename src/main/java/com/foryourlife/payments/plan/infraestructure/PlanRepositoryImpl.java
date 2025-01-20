package com.foryourlife.payments.plan.infraestructure;

import com.foryourlife.payments.plan.domain.Plan;
import com.foryourlife.payments.plan.domain.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanRepositoryImpl implements PlanRepository {
    private final JPAPlanRepository repository;

    public PlanRepositoryImpl(JPAPlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Plan plan) {
        this.repository.save(plan);
    }

    @Override
    public void saveAll(List<Plan> plans) {
        this.repository.saveAll(plans);
    }

    @Override
    public List<Plan> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Plan> findById(String id) {
        return this.repository.findById(id);
    }
}
