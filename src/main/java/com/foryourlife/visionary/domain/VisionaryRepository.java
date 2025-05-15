package com.foryourlife.visionary.domain;

import java.time.LocalDate;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VisionaryRepository {
    void save(Visionary visionary);

    Optional<Visionary> findById(String id);

    void deleteById(String id);

    List<Visionary> findAll();
    Page<Visionary> findAll(Pageable pageable);
    Page<Visionary> findAll(Pageable pageable,Criteria criteria);

    List<Visionary> findAvailableVisionaries(LocalDate startDate, LocalDate endDate);

    List<Visionary> match(Criteria criteria);

    Optional<Visionary> findByUserId(String userId);

    boolean isVisionaryAvailable(String visionaryId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
