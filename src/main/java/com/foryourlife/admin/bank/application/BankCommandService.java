package com.foryourlife.admin.bank.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.bank.domain.Bank;
import com.foryourlife.admin.bank.domain.BankRepository;
import com.foryourlife.admin.bank.infrastructure.http.BankRequest;
import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankCommandService {
    private final BankRepository repository;
    private QueryCampusService queryCampusService;
    private final RestClient client;
    private final ConfigContificoQueryService config;

    public BankCommandService(BankRepository repository, QueryCampusService queryCampusService, RestClient client, ConfigContificoQueryService config) {
        this.repository = repository;
        this.queryCampusService = queryCampusService;
        this.client = client;
        this.config = config;
    }

    public void syncBanks(String campusId) {
        var configContifico = config.findConfigContificoByCampusId(campusId);
        try {
            ResponseEntity<String> response = client.get().uri("https://api.contifico.com/sistema/api/v1/banco/cuenta/").header("Content-Type", "application/json").header("Api-Token", configContifico.getApiKey()).header("Authorization", configContifico.getApiSecret()).retrieve().toEntity(String.class);
            if (response.getStatusCode().value() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                if (rootNode.isArray()) {
                    for (JsonNode contificoBank : rootNode) {
                        String contificoId = contificoBank.get("id").asText();
                        String number = contificoBank.get("numero").asText();
                        String name = contificoBank.get("nombre").asText();

                        Optional<Bank> existingBank = repository.findByContificoId(contificoId);

                        if (existingBank.isEmpty()) {
                            Bank newBank = new Bank(UUID.randomUUID().toString(), name, number, contificoId, configContifico.getCampus());

                            repository.saveBank(newBank);
                        }
                    }

                } else {
                    throw new BaseException("Failed to fetch products from Contifico API", List.of(""));
                }
            }
        } catch (Exception e) {
            throw new BaseException("Error syncing banks", List.of(""));
        }
    }

    public void createBank(BankRequest bank) {
        var campus = queryCampusService.findById(bank.getCampusId());
        var newBank = new Bank(!bank.getId().isEmpty() && bank.getId() != null ? bank.getId() : UUID.randomUUID().toString(), bank.getName(), bank.getNumber(), null, campus);
        repository.saveBank(newBank);
    }
}
