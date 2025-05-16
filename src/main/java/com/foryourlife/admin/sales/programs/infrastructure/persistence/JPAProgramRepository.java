package com.foryourlife.admin.sales.programs.infrastructure.persistence;

import com.foryourlife.admin.sales.programs.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAProgramRepository extends JpaRepository<Program, String>, JpaSpecificationExecutor<Program> {
}
