package com.foryourlife.admin.sales.invoices.domain;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.product.domain.Product;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    private String id;
    private String fullName;
    private String address;
    private String document;
    private String phone;
    private String email;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<Product> products;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "is_sent_contifico")
    private Boolean isSentContifico;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;
    @Column(name = "contifico_id")
    private String contificoId;
    @Column(name = "contifico_error")
    private String contificoError;
    @Column(name = "client_type")
    private String clientType;
    private Double tax;
    private BigDecimal amount;
    @Column(columnDefinition = "jsonb", name = "invoice_contifico_json")
    @Type(JsonType.class)
    private InvoiceContificoJson invoiceContifico;

    protected Invoice() {
    }

    public Invoice(String id, String fullName, String address, String document, String phone, String email, String invoiceNumber, LocalDateTime invoiceDate, List<Product> products, Payment payment, Boolean sentSri, BigDecimal taxAmount, BigDecimal totalDiscount, Double tax, BigDecimal amount, String clientType) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.products = products;
        this.payment = payment;
        this.isSentContifico = sentSri;
        this.taxAmount = taxAmount;
        this.totalDiscount = totalDiscount;
        this.tax = tax;
        this.amount = amount;
        this.clientType = clientType;
    }

    public static Invoice create(String id, String fullName, String address, String document, String phone, String email, String invoiceNumber, LocalDateTime invoiceDate, List<Product> products, Payment payment, Boolean sentSri, BigDecimal taxAmount, BigDecimal totalDiscount, Double tax, BigDecimal amount, String clientType) {
        return new Invoice(id, fullName, address, document, phone, email, invoiceNumber, invoiceDate, products, payment, sentSri, taxAmount, totalDiscount, tax, amount, clientType);
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

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getSentContifico() {
        return isSentContifico;
    }

    public void setSentContifico(Boolean sentContifico) {
        isSentContifico = sentContifico;
    }

    public InvoiceContificoJson getInvoiceContifico() {
        return invoiceContifico;
    }

    public void setInvoiceContifico(InvoiceContificoJson invoiceContifico) {
        this.invoiceContifico = invoiceContifico;
    }

    public String getContificoId() {
        return contificoId;
    }

    public void setContificoId(String contificoId) {
        this.contificoId = contificoId;
    }

    public String getContificoError() {
        return contificoError;
    }

    public void setContificoError(String contificoError) {
        this.contificoError = contificoError;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
