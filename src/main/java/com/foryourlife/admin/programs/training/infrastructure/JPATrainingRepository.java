package com.foryourlife.admin.programs.training.infrastructure;

import com.foryourlife.admin.programs.training.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPATrainingRepository extends JpaRepository<Training, String> {
}
