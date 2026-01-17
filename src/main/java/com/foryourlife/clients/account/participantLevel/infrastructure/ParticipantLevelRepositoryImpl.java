package com.foryourlife.clients.account.participantLevel.infrastructure;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantLevelRepositoryImpl implements ParticipantLevelRepository {

    private final JPAParticipantLevelRepository repository;

    public ParticipantLevelRepositoryImpl(JPAParticipantLevelRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<ParticipantLevel> roles) {
        this.repository.saveAll(roles);
    }

    @Override
    public Optional<ParticipantLevel> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public List<ParticipantLevel> getAll() {
        return this.repository.findAll();
    }

    @Override
    public List<ParticipantLevel> findByCriteria(Specification<ParticipantLevel> specification) {
        return this.repository.findAll(specification);
    }

    @Override
    public Optional<ParticipantLevel> findOneByCriteria(Specification<ParticipantLevel> specification) {
        return this.repository.findOne(specification);
    }

    @Override
    public Optional<ParticipantLevel> findByCourseLevelId(CourseLevel courseLevel) {
        return repository.findByCourseLevel(courseLevel);
    }
}
