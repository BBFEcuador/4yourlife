package com.foryourlife.admin.training.program.infrastructure;

import com.foryourlife.admin.training.program.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPATrainingRepository extends JpaRepository<Training, String> {
}
