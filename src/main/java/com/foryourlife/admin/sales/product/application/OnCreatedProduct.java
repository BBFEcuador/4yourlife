package com.foryourlife.admin.sales.product.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.ProductCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@DomainEventSubscriber(value = {ProductCreated.class})
public class OnCreatedProduct {

    private final ObjectMapper objectMapper;
    private final RestClient httpClient;
    private final ProductRepository productRepository;
    private final ConfigContificoRepository configContificoRepository;

    public OnCreatedProduct(ObjectMapper objectMapper, RestClient httpClient, ProductRepository productRepository, ConfigContificoRepository configContificoRepository) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.productRepository = productRepository;
        this.configContificoRepository = configContificoRepository;
    }

    @EventListener
    public void handle(ProductCreated event) {
        try {
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("minimo", event.getProduct().getBasePrice());
            jsonNode.put("pvp1", event.getProduct().getBasePrice());
            jsonNode.put("nombre", event.getProduct().getName());
            jsonNode.put("estado", "A");
            jsonNode.put("codigo", event.getProduct().getId().substring(0, 21));
            jsonNode.put("tipo", "SER");
            jsonNode.put("para_pos", true);
            jsonNode.put("porcentaje_iva", event.getProduct().getDescription());
            jsonNode.put("tipo_producto", "SIM");

            String json = jsonNode.toString();

            var config = configContificoRepository.findByCampusId(event.getProduct().getCampus().getId()).orElseThrow(
                    () -> new BaseException("Config not found", List.of(""))
            );

            ResponseEntity<String> response = httpClient.post()
                    .uri("https://api.contifico.com/sistema/api/v1/producto/")
                    .body(json)
                    .header("Api-Token", config.getApiKey())
                    .header("Authorization", config.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);
            System.out.println(response.getBody());
            if (response.getStatusCodeValue() > 299 || response.getStatusCodeValue() < 200) {
                throw new RuntimeException("Failed to create product in Contifico API: " + response.getBody());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            Product product = event.getProduct();
            product.setContificoId(rootNode.get("id").asText());
            productRepository.save(product);

            System.out.println("Status code: " + response.getStatusCodeValue());
            System.out.println("Response body: " + response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}