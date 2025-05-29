package com.foryourlife.admin.sales.invoices.infraestructure.http;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InvoiceRequest {

    public String id;
    @NotNull(message = "El numero de factura es requerido")
    public String invoiceNumber;
    @NotNull(message = "La fecha de la factura es requerida")
    public LocalDate invoiceDate;
    @NotNull(message = "Los datos de la factura son requeridos")
    public DataInvoice dataInvoice;
    @NotNull(message = "Los productos son requeridos")
    public List<Product> products;
    @NotNull(message = "El pago es requerido")
    public Payment payment;
    public Boolean sentSri;

    public Invoice toDomain() {
        return Invoice.create(
                id != null ? id : UUID.randomUUID().toString(),
                invoiceNumber,
                invoiceDate,
                dataInvoice,
                products,
                payment,
                sentSri
        );
    }
}
