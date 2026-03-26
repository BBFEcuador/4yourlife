package com.foryourlife.admin.crm.call.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CallRepository {
    void save(Call call);
    Page<Call> findAll(Pageable p, Criteria criteria);
    Optional<Call> findById(String id);
    List<Call> findAllByTrainingId(String trainingId);
    void saveAll(List<Call> calls);
}
