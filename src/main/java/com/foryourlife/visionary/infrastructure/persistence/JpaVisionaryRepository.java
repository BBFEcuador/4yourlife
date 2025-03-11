package com.foryourlife.visionary.infrastructure.persistence;

import com.foryourlife.visionary.domain.Visionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaVisionaryRepository extends JpaRepository<Visionary, String>, JpaSpecificationExecutor<Visionary> {
    List<Visionary> findAllByUser_Id(String userId);
}
