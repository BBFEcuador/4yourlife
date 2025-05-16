package com.foryourlife.admin.sales.discounts.applications;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscountRepository;
import com.foryourlife.admin.sales.discounts.infrastructure.http.ChangeStatusRequest;
import com.foryourlife.admin.sales.discounts.infrastructure.http.ProductDiscountRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductDiscountCommandService  {
    private final ProductDiscountRepository productDiscountRepository;
    public ProductDiscountCommandService(ProductDiscountRepository productDiscountRepository) {
        this.productDiscountRepository = productDiscountRepository;
    }

    public void save(ProductDiscountRequest request) {
        var productDiscount = ProductDiscount.create(
                request.id != null ? request.id : UUID.randomUUID().toString(),
                request.name,
                request.discountType,
                Float.parseFloat(request.discountValue),
                request.needSupervision,
                request.isActive
        );
        productDiscountRepository.save(productDiscount);
    }

    public void changeStatus(ChangeStatusRequest request) {
        var productDiscount = productDiscountRepository.findById(request.id)
                .orElseThrow(() -> new IllegalArgumentException("Product discount not found"));
        productDiscount.changeStatus(request.status);
        productDiscountRepository.save(productDiscount);
    }
}
