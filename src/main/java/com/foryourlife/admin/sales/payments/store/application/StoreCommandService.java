package com.foryourlife.admin.sales.payments.store.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.admin.sales.payments.store.domain.StoreRepository;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreCommandService {
    private final StoreRepository repository;
    private final RestClient client;
    private final ConfigContificoRepository configContificoRepository;

    public StoreCommandService(StoreRepository repository, RestClient client, ConfigContificoRepository configContificoRepository) {
        this.repository = repository;
        this.client = client;
        this.configContificoRepository = configContificoRepository;
    }

    public void createStore(String campusId) {
        List<Product> syncedProducts = new ArrayList<>();
        var config = configContificoRepository.findByCampusId(campusId).orElseThrow(
                () -> new BaseException("Config not found", List.of(""))
        );

        try {
            ResponseEntity<String> response = client.get()
                    .uri("https://api.contifico.com/sistema/api/v1/producto/")
                    .header("Content-Type", "application/json")
                    .header("Api-Token", config.getApiKey())
                    .header("Authorization", config.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode().value() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                if (rootNode.isArray()) {
//                    for (JsonNode contificoPos : rootNode) {
//                        var contificoPos =
//                    }
                }
            }
//                repository.save(new Store());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("Error sincronizando con contifico", List.of(""));
        }
    }
}
