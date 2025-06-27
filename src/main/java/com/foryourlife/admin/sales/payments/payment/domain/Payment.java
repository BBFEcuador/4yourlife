package com.foryourlife.admin.sales.payments.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.PaymentCreated;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "payments")
public class Payment extends AggregateRoot {
    @Id
    private String id;

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "id")
    private ProductDiscount discount;
    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;
    @ManyToOne
    @JoinColumn(name = "campus_id", referencedColumnName = "id")
    private Campus campus;

    //    @Convert(converter = PaymentHistoryConverter.class)
    @Column(columnDefinition = "jsonb", name = "payments_history")
    @Type(JsonType.class)
    private List<PaymentHistory> paymentshistory;

    private Double total;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String  note;

    @OneToMany(
            mappedBy = "payment"
    )
    @JsonIgnoreProperties("payment")
    private List<CashDrawerDetail> cashDrawerDetail;

    protected Payment() {
    }

    public Payment(String id, List<Product> products, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, Double total, PaymentStatus status, String note) {
        this.id = id;
        this.products = products;
        this.discount = discount;
        this.participant = participant;
        this.campus = campus;
        this.paymentshistory = paymentshistory;
        this.total = total;
        this.status = status;
        this.note = note;
    }

    public static Payment create(String id, List<Product> product, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, Double total, PaymentStatus status, String note) {
        return new Payment(id, product, discount, participant, campus, paymentshistory, total, status, note);
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

    public Double getTotal() {
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
}
