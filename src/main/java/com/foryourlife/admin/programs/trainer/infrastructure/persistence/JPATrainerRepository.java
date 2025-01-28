package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPATrainerRepository extends JpaRepository<Trainer, String> {
}
