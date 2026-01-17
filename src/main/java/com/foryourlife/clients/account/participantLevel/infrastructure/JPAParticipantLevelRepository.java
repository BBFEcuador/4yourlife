package com.foryourlife.clients.account.participantLevel.infrastructure;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPAParticipantLevelRepository extends JpaRepository<ParticipantLevel,String>, JpaSpecificationExecutor<ParticipantLevel> {
    Optional<ParticipantLevel> findById(String id);
    List<ParticipantLevel> findAll(Specification<ParticipantLevel> specification);
    Optional<ParticipantLevel> findByRoleName(String roleName);

    Optional<ParticipantLevel> findByCourseLevel(CourseLevel courseLevel);
}
