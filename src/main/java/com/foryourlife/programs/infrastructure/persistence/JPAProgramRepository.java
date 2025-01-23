package com.foryourlife.programs.infrastructure.persistence;

import com.foryourlife.programs.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAProgramRepository extends JpaRepository<Program, String>, JpaSpecificationExecutor<Program> {
}
