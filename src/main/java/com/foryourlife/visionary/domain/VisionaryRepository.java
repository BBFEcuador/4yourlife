package com.foryourlife.visionary.domain;

import com.foryourlife.shared.domain.criteria.Criteria;

import java.util.List;
import java.util.Optional;

public interface VisionaryRepository {
    void save(Visionary visionary);

    Optional<Visionary> findById(String id);

    void deleteById(String id);

    List<Visionary> findAll();

    List<Visionary> match(Criteria criteria);
}
