package com.foryourlife.admin.contifico.config.domain;

import java.util.List;
import java.util.Optional;

public interface ConfigContificoRepository {
    ConfigContifico save(ConfigContifico configContifico);

    Optional<ConfigContifico> findByCampusId(String id);

    Optional<ConfigContifico> findById(String id);

    void deleteById(String id);

    List<ConfigContifico> findAll();
}
