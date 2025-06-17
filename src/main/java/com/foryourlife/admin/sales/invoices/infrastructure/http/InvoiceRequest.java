package com.foryourlife.admin.sales.invoices.infrastructure.http;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InvoiceRequest {

    public String id;
    @NotNull(message = "El nombre completo es requerido")
    @NotBlank(message = "El nombre completo es requerido")
    public String fullName;
    @NotNull(message = "La dirección es requerida")
    @NotBlank(message = "La dirección es requerida")
    public String address;
    @NotNull(message = "El número de documento es requerido")
    @NotBlank(message = "El número de documento es requerido")
    public String document;
    @NotNull(message = "El número de teléfono es requerido")
    @NotBlank(message = "El número de teléfono es requerido")
    public String phone;
    @NotNull(message = "El email es requerido")
    @NotBlank(message = "El email es requerido")
    public String email;

    public List<Product> products;
    public Payment payment;
    public Boolean sentSri;
    public String invoiceNumber;
    public LocalDate invoiceDate;
    public Double amount;


    public Invoice toDomain() {
        return Invoice.create(
                id != null ? id : UUID.randomUUID().toString(),
                fullName,
                address,
                document,
                phone,
                email,
                invoiceNumber,
                invoiceDate != null ? invoiceDate : LocalDate.now(),
                products != null ? products : List.of(),
                payment,
                sentSri,
                (amount*0.15),
                15.0,
                amount
        );
    }
}
