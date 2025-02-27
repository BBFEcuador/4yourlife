package com.foryourlife.admin.programs.training.infrastructure;

import com.foryourlife.admin.programs.training.domain.StartDate;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JPATrainingRepository extends JpaRepository<Training, String> , JpaSpecificationExecutor<Training> {
    List<Training> findByStartDate(StartDate startDate);
}
