package com.foryourlife.clients.account.promises.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PromiseRepository {
    Promise save(Promise promise);
    Optional<Promise> findById(String id);
    void deleteById(String id);
    Page<Promise> findAll(Pageable pageable, Criteria criteria);
    List<Promise> findByTrainingId(String trainingId);
    Optional<Promise> findLastByUserId(String userId);
    Optional<Promise> findLastByUserIdAndTrainingId(String userId, String trainingId);
    void saveAll(List<Promise> promises);
}
