package com.foryourlife.admin.sales.payments.domain;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "id")
    private ProductDiscount discount;
    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;
    @ManyToOne
    @JoinColumn(name = "campus_id", referencedColumnName = "id")
    private Campus campus;

    @Convert(converter = PaymentHistoryConverter.class)
    @Column(columnDefinition = "jsonb", name = "payments_history")
    private List<PaymentHistory> paymentshistory;

    private Double total;
    private String status;

    protected Payment() {
    }

    private Payment(String id, Product product, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, Double total, String status) {
        this.id = id;
        this.product = product;
        this.discount = discount;
        this.participant = participant;
        //this.campus = campus;
        this.paymentshistory = paymentshistory;
        this.total = total;
        this.status = status;
    }

    public Payment create(String id, Product product, ProductDiscount discount, Participant participant, Campus campus, List<PaymentHistory> paymentshistory, Double total, String status){
        return new Payment(id, product, discount, participant, campus, paymentshistory, total, status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(ProductDiscount discount) {
        this.discount = discount;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

   /* public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }*/

    public List<PaymentHistory> getPaymentshistory() {
        return paymentshistory;
    }

    public void setPaymentshistory(List<PaymentHistory> paymentshistory) {
        this.paymentshistory = paymentshistory;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
