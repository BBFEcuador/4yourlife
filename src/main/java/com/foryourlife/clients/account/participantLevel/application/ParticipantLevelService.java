package com.foryourlife.clients.account.participantLevel.application;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantLevelService {
    private final ParticipantLevelRepository repository;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public ParticipantLevelService(ParticipantLevelRepository repository) {
        this.repository = repository;
    }

    public ParticipantLevel getInitRole() throws BaseException {
        Specification<ParticipantLevel> specification = (root, query, cb) -> cb.equal(root.get("isStarted").as(Boolean.class), true);
        return this.repository.findOneByCriteria(specification).orElseThrow(() ->
                new BaseException("Role Problem", List.of("The initial role is not on database"))
        );
    }

    public ParticipantLevel getRolByLevel(CourseLevel courseLevel) throws BaseException {
        Specification<ParticipantLevel> specification = (root, query, cb) -> cb.equal(root.get("courseLevel"), cb.literal(courseLevel.toString()));
        return this.repository.findOneByCriteria(specification).orElseThrow(() ->
                new BaseException("Role Problem", List.of("The " + courseLevel.toString() + " role is not on database"))
        );
    }

    public ParticipantLevel getRoleById(String id) {
        return this.repository.findById(id).orElseThrow(() ->
                new BaseException("Role Not found", List.of("The role with id " + id + " does not exist"))
        );
    }
}
