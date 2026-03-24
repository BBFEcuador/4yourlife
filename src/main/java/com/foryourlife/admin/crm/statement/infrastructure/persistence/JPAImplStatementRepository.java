package com.foryourlife.admin.crm.statement.infrastructure.persistence;

import com.foryourlife.admin.crm.statement.domain.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAImplStatementRepository  extends JpaRepository<Statement,String> {
}
