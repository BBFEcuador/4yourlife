package com.foryourlife.admin.contifico.config.application;

import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigContificoQueryService {
    private final ConfigContificoRepository repository;

    public ConfigContificoQueryService(ConfigContificoRepository repository) {
        this.repository = repository;
    }

    public ConfigContifico findConfigContificoByCampusId(String campusId) {
        return repository.findByCampusId(campusId)
                .orElseThrow(() -> new BaseException("ConfigContifico not found for campus ID: " + campusId, List.of("No configuration found for the specified campus.")));
    }

    public List<ConfigContifico> getAllConfigContificos() {
        return repository.findAll();
    }

    public ConfigContifico findConfigContificoById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BaseException("ConfigContifico not found for ID: " + id, List.of("No configuration found for the specified ID.")));
    }
}
