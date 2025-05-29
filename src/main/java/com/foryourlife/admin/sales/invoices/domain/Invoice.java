package com.foryourlife.admin.sales.invoices.domain;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
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
    @Column(name = "invoice_date")
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

    @Column(name = "sent_sri")
    private Boolean sentSri;

    protected Invoice() {
    }

    private Invoice(String id, String invoiceNumber, LocalDate invoiceDate, DataInvoice dataInvoice, List<Product> products, Payment payment, Boolean sentSri) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dataInvoice = dataInvoice;
        this.products = products;
        this.payment = payment;
        this.sentSri = sentSri;
    }

    public static Invoice create(String id, String invoiceNumber, LocalDate invoiceDate, DataInvoice dataInvoice, List<Product> products, Payment payment, Boolean sentSri) {
        return new Invoice(id, invoiceNumber, invoiceDate, dataInvoice, products, payment, sentSri);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public DataInvoice getDataInvoice() {
        return dataInvoice;
    }

    public void setDataInvoice(DataInvoice dataInvoice) {
        this.dataInvoice = dataInvoice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Boolean getSentSri() {
        return sentSri;
    }

    public void setSentSri(Boolean sentSri) {
        this.sentSri = sentSri;
    }
}
