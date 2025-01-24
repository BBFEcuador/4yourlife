package com.foryourlife.admin.training.campus.domain;

import java.util.List;
import java.util.Optional;

public interface CampusRepository {
    void save(Campus campus);
    void update(Campus campus);
    List<Campus> getAll();
    Optional<Campus> findById(String id);
}
