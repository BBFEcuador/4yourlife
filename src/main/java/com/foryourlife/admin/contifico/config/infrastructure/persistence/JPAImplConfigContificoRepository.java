package com.foryourlife.admin.contifico.config.infrastructure.persistence;

import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAImplConfigContificoRepository extends JpaRepository<ConfigContifico, String> {
    <T> Optional<ConfigContifico> findByCampus_Id(String campusId);
}
