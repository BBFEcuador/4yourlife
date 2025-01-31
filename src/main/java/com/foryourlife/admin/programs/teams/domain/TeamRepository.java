package com.foryourlife.admin.programs.teams.domain;

import net.datafaker.providers.food.Tea;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    void save(Team team);
    void updatePhoto(String id, String photo);
    Optional<Team> findById(String id);
    List<Team> findAll();
    void assignParticipants(String teamId, String userId);
    void assignMastersLife(String teamId, String userId);
    void removeParticipants(String teamId, String userId);
    void removeMastersLife(String teamId, String userId);

}
