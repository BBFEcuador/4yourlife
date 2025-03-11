package com.foryourlife.visionary.infrastructure.persistence;

import com.foryourlife.visionary.domain.Visionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVisionaryRepository extends JpaRepository<Visionary, String>, JpaSpecificationExecutor<Visionary> {
    Visionary findByUser_Id(String userId);
}
