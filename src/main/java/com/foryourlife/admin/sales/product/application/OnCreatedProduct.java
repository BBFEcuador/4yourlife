package com.foryourlife.admin.sales.product.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.ProductCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@DomainEventSubscriber(value = {ProductCreated.class})
public class OnCreatedProduct {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final ProductRepository productRepository;

    public OnCreatedProduct(ObjectMapper objectMapper, ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.httpClient = HttpClient.newHttpClient();
    }

    @EventListener
    public void handle(ProductCreated event) {
        try {
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("minimo", event.getProduct().getBasePrice());
            jsonNode.put("pvp1", event.getProduct().getBasePrice());
            jsonNode.put("nombre", event.getProduct().getName());
            jsonNode.put("estado", "A");
            jsonNode.put("codigo", event.getProduct().getId());

            String json = jsonNode.toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.contifico.com/sistema/api/v1/producto/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()!= 200) {
                throw new RuntimeException("Failed to create product in Contifico API: " + response.body());
            }

            String productCode = event.getProduct().getId();
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.contifico.com/sistema/api/v1/producto/?codigo=" + productCode))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (getResponse.statusCode() != 200) {
                throw new RuntimeException("Failed to get product from Contifico API: " + getResponse.body());
            }

            ObjectNode responseNode = (ObjectNode) objectMapper.readTree(getResponse.body());
            String contificoId = responseNode.get("id").asText();

            Product product = event.getProduct();
            product.setContificoId(contificoId);
            productRepository.save(product);

            System.out.println("Status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
