package com.foryourlife.admin.programs.training.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    void saveAll(List<Training> trainings);
    void save(Training trainings);
    Page<Training> getAll(Pageable pageable, Criteria criteria);
    List<Training> match(Criteria criteria);
    Training matchOne(Criteria criteria);
    List<Training> findByStartDate(LocalDate date);
    List<Training> findByStartDateAndCampus_id(LocalDate date,String campusId);
    Optional<Training> findById(String id);
    Optional<Training> findByNextLevel_Id(String id);
}
