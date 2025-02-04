package com.foryourlife.admin.statement.infrastructure.persistence;

import com.foryourlife.admin.statement.domain.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAStatementRepository extends JpaRepository<Statement, String> {
}
