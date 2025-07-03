package com.foryourlife.admin.sales.product.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductCreateService {
    private final EventBus eventBus;
    private final RestClient client;
    private final ProductRepository repository;
    private final ConfigContificoRepository configContificoRepository;

    public ProductCreateService(EventBus eventBus, RestClient client, ProductRepository repository, ConfigContificoRepository configContificoRepository) {
        this.eventBus = eventBus;
        this.client = client;
        this.repository = repository;
        this.configContificoRepository = configContificoRepository;
    }

    public void saveProduct(Product product) {
        this.repository.save(product);
        eventBus.publish(product.pullDomainEvents());
    }

    public void disableProductById(String id) {
        var product = repository.findById(id).orElseThrow(() -> {
            ;
            return new IllegalArgumentException("Product doesn't exist with id: " + id);
        });
        product.setActive(!product.getActive());
        repository.save(product);
    }

    public void updateProduct(Product product) {
        repository.findById(product.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("Product doesn't exist with id: " + product.getId());
        });
        repository.save(product);
    }

    public List<Product> syncProducts(String campusId) {
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
                    for (JsonNode contificoProduct : rootNode) {
                        String contificoId = contificoProduct.get("id").asText();
                        String code = contificoProduct.get("codigo").asText();
                        String name = contificoProduct.get("nombre").asText();
                        double price = contificoProduct.get("pvp1").asDouble();

                        Optional<Product> existingProduct = repository.findByContificoId(contificoId);

                        if (existingProduct.isEmpty()) {
                            Product newProduct = new Product(
                                    UUID.randomUUID().toString(),
                                    code,
                                    name,
                                    price,
                                    "DOLAR",
                                    true,
                                    null,
                                    null,
                                    null,
                                    config.getCampus(),
                                    contificoId
                            );

                            newProduct.setContificoId(contificoId);
                            newProduct.setCode(code);
                            newProduct.setName(name);
                            newProduct.setBasePrice(price);
                            newProduct.setActive(true);

                            repository.save(newProduct);
                            syncedProducts.add(newProduct);
                        } else {
                            syncedProducts.add(existingProduct.get());
                        }
                    }
                }

            } else {
                throw new BaseException("Failed to fetch products from Contifico API", List.of("")  );
            }
            return syncedProducts;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("Error sincronizando con contifico", List.of(""));
        }

    }
}
