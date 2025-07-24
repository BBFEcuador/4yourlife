package com.foryourlife.admin.sales.invoices.infrastructure.http;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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
    @Pattern(regexp = "^\\d{10}$|^\\d{13}$", message = "El documento debe tener 10 o 13 dígitos numericos")
    @Pattern(regexp = "^[0-9]*$", message = "El documento solo acepta dígitos")
    public String document;
    @NotNull(message = "El número de teléfono es requerido")
    @NotBlank(message = "El número de teléfono es requerido")
    @Pattern(regexp = "^[0-9]{10}$", message = "El número de teléfono debe tener 10 dígitos numericos")
    public String phone;
    @NotNull(message = "El email es requerido")
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email es inválido")
    public String email;
    @NotNull(message = "El tipo de persona es requerido")
    @NotBlank(message = "El tipo de persona es requerido")
    public String type;

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
