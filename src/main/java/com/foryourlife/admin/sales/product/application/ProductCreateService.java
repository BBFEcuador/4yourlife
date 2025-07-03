package com.foryourlife.admin.sales.product.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductCreateService {
    private final EventBus eventBus;
    private final HttpClient client;
    private final ProductRepository repository;

    public ProductCreateService(EventBus eventBus, HttpClient client, ProductRepository repository) {
        this.eventBus = eventBus;
        this.client = client;
        this.repository = repository;
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

    public List<Product> syncProducts() {
        List<Product> syncedProducts = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.contifico.com/sistema/api/v1/producto/"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());

                if (rootNode.isArray()) {
                    for (JsonNode contificoProduct : rootNode) {
                        String contificoId = contificoProduct.get("id").asText();
                        String code = contificoProduct.get("codigo").asText();
                        String name = contificoProduct.get("nombre").asText();
                        double price = contificoProduct.get("pvp1").asDouble();

                        Product product = repository.findByContificoId(contificoId)
                                .orElse(new Product(
                                        UUID.randomUUID().toString(),
                                        code,
                                        name,
                                        price,
                                        "DOLAR",
                                        true,
                                        null,
                                        null,
                                        null,
                                        null,
                                        contificoId
                                ));

                        product.setContificoId(contificoId);
                        product.setCode(code);
                        product.setName(name);
                        product.setBasePrice(price);
                        product.setActive(true);

                        repository.save(product);
                        syncedProducts.add(product);
                    }
                }

            } else {
                throw new RuntimeException("Failed to fetch products from Contifico API: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error syncing products with Contifico", e);
        }

        return syncedProducts;
    }
}
