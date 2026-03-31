package com.foryourlife.admin.crm.statement.infrastructure.persistence;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JPAImplStatementRepository  extends JpaRepository<Statement,String>, JpaSpecificationExecutor<Statement> {
    List<Statement> findByTraining_Id(String trainingId);

    Optional<Statement> findByParticipant_IdAndCourseLevel(String participantId, CourseLevel courseLevel);
}
