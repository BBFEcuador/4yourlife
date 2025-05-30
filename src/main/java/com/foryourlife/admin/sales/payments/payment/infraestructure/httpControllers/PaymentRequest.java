package com.foryourlife.admin.sales.payments.payment.infraestructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentRequest {
    public String id;
    
    @NotNull(message = "Los productos son requeridos")
    public List<String> products;
    public ProductDiscount discount;
    @NotNull(message = "El Participante es requerido")
    public Participant participant;
    @NotNull(message = "El campus es requerido")
    public Campus campus;
    @NotNull(message = "Pago es requerido")
    public List<PaymentHistory> paymentshistory;
    @NotNull(message = "El valor total es requerido")
    public Double total;
    public PaymentStatus status;
    @NotNull(message = "Los datos de facturación son requeridos")
    public DataInvoice dataInvoice;
    public String note;
}
