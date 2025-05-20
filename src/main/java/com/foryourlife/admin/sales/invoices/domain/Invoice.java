package com.foryourlife.admin.sales.invoices.domain;

import com.foryourlife.admin.sales.payments.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    private String id;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    private LocalDate invoiceDate;

    @ManyToOne
    @JoinColumn(name = "dataInvoice_id", referencedColumnName = "id")
    private DataInvoice dataInvoice;

    @Column( columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<Product> products;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;
}
