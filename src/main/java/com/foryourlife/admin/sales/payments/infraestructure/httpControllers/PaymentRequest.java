package com.foryourlife.admin.sales.payments.infraestructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.payments.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.domain.PaymentHistoryConverter;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentRequest {
    public String id;
    
    @NotNull(message = "Los productos son requeridos")
    public List<Product> product;
    
    public ProductDiscount discount;
    @NotNull(message = "El Participante es requerido")
    public Participant participant;
    @NotNull(message = "El campus es requerido")
    public Campus campus;
    public List<PaymentHistory> paymentshistory;
    @NotNull(message = "El valor total es requerido")
    public Double total;
    public String status;
}
