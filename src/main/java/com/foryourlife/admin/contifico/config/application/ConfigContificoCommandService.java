package com.foryourlife.admin.contifico.config.application;

import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.contifico.config.infrastructure.http.ConfigContificoRequest;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfigContificoCommandService {
    private final ConfigContificoRepository repository;
    private final QueryCampusService queryCampusService;

    public ConfigContificoCommandService(ConfigContificoRepository repository, QueryCampusService queryCampusService) {
        this.repository = repository;
        this.queryCampusService = queryCampusService;
    }

    public void createConfigContifico(ConfigContificoRequest configContificoRequest) {
        var campus = queryCampusService.findById(configContificoRequest.campusId);
        ConfigContifico configContifico = new ConfigContifico(
                configContificoRequest.id == null ?  UUID.randomUUID().toString(): configContificoRequest.id,
                configContificoRequest.apiKey,
                configContificoRequest.apiSecret,
                campus
        );
        repository.save(configContifico);
    }

    public void deleteConfigContifico(String id) {
        repository.deleteById(id);
    }
}
