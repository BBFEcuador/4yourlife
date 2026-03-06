package com.foryourlife.admin.sales.payments.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "payments")
public class Payment extends AuditableEntity {

    @Id
    private String id;

    @JsonIgnoreProperties({"rules", "campus"})
    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"payments"})
    private ProductDiscount discount;

    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"campus", "profile", "participantLevel", "invitationToken", "isLingerer", "isDesertor", "modules", "contacts", "team", "teams", "medicalRecord", "user.entityMap", "modules", "contacts"})
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "campus_id", referencedColumnName = "id")
    private Campus campus;

    @Column(columnDefinition = "jsonb", name = "payments_history")
    @Type(JsonType.class)
    private List<PaymentHistory> paymentshistory;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String note;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CashDrawerDetail> cashDrawerDetail;

    @OneToMany(mappedBy = "payment", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"payment", "invoiceContifico", "products"})
    private List<Invoice> invoices = Collections.emptyList();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "training_id",
            referencedColumnName = "id"
    )
    private Training training;

    protected Payment() {
    }

    public Payment(String id, List<Product> products, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, BigDecimal total, PaymentStatus status, String note, Training training) {
        this.id = id;
        this.products = products;
        this.discount = discount;
        this.participant = participant;
        this.campus = campus;
        this.paymentshistory = paymentshistory;
        this.total = total;
        this.status = status;
        this.note = note;
        this.training = training;
    }

    public static Payment create(String id, List<Product> product, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, BigDecimal total, PaymentStatus status, String note, Training training) {
        return new Payment(id, product, discount, participant, campus, paymentshistory, total, status, note, training);
    }

    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public ProductDiscount getDiscount() {
        return discount;
    }

    public Participant getParticipant() {
        return participant;
    }

    public Campus getCampus() {
        return campus;
    }

    public List<PaymentHistory> getPaymentshistory() {
        return paymentshistory;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at.getDayOfMonth() + "/" + created_at.getMonthValue() + "/" + created_at.getYear();
    }

    public List<Invoice> getInvoice() {
        return invoices;
    }

    public Boolean getHasSomePaymentWithError() {
        return this.invoices.stream().anyMatch(invoice -> !invoice.getSentContifico());
    }

    public void setPaymentshistory(List<PaymentHistory> paymentshistory) {
        this.paymentshistory = paymentshistory;
    }

    @JsonProperty("remainingBalance")
    public BigDecimal getRemainingBalance() {
        BigDecimal totalPayments = BigDecimal.valueOf(this.paymentshistory.stream()
                .mapToDouble(PaymentHistory::getAmount)
                .sum());
        return this.total.subtract(totalPayments);
    }
}
