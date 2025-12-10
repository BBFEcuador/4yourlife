package com.foryourlife.admin.programs.teams.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import net.datafaker.providers.food.Tea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    void save(Team team);
    void updatePhoto(String id, String photo);
    Optional<Team> findById(String id);
    Optional<Team> findByTrainingId(String id);
    List<Team> findAll();
    Page<Team> findAll(Pageable pageable, Criteria criteria);
    List<Team> match(Criteria criteria);
    void assignParticipants(String teamId, String userId);
    void assignMastersLife(String teamId, String userId);
    void removeParticipants(String teamId, String userId);
    void removeMastersLife(String teamId, String userId);
    void removeStaffs(String teamId, String userId);
    List<Team> findByTrainerId(String trainerId);
    void removeVisionaries(String teamId, String id);
    Long countTeams();
}
