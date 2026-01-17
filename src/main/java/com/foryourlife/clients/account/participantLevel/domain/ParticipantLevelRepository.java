package com.foryourlife.clients.account.participantLevel.domain;

import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ParticipantLevelRepository {
    void saveAll(List<ParticipantLevel> roles);
    List<ParticipantLevel> getAll();
    List<ParticipantLevel> findByCriteria(Specification<ParticipantLevel> specification);
    Optional<ParticipantLevel> findOneByCriteria(Specification<ParticipantLevel> specification);
    Optional<ParticipantLevel> findById(String id);
    Optional<ParticipantLevel> findByCourseLevelId(CourseLevel courseLevel);
}
