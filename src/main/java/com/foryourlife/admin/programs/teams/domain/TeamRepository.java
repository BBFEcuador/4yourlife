package com.foryourlife.admin.programs.teams.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import net.datafaker.providers.food.Tea;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    void save(Team team);
    void updatePhoto(String id, String photo);
    Optional<Team> findById(String id);
    Optional<Team> findByTrainingId(String id);
    List<Team> findAll();
    List<Team> match(Criteria criteria);
    void assignParticipants(String teamId, String userId);
    void assignMastersLife(String teamId, String userId);
    void removeParticipants(String teamId, String userId);
    void removeMastersLife(String teamId, String userId);
    void removeStaffs(String teamId, String userId);

    void removeVisionaries(String teamId, String id);
}
