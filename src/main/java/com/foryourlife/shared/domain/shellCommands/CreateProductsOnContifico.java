package com.foryourlife.shared.domain.shellCommands;

import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.ProductCreated;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@ShellComponent
@Service
public class CreateProductsOnContifico {
    private final ProductRepository productRepository;
    private final EventBus eventBus;

    public CreateProductsOnContifico(ProductRepository productRepository, EventBus eventBus) {
        this.productRepository = productRepository;
        this.eventBus = eventBus;
    }

    @ShellMethod(key = "app:create-products-on-contifico")
    public void createProductsOnContifico() {
        System.out.println("Creating products on Contifico...");
        productRepository.findAll().forEach(
                product -> {
                    if (product.getContificoId() == null || product.getContificoId().isEmpty()) {

                        eventBus.publish(List.of(new ProductCreated(product)));
                    }
                }
        );
    }
}
