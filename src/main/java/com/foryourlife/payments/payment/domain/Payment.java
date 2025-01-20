package com.foryourlife.payments.payment.domain;

import com.foryourlife.payments.plan.domain.Plan;
import com.foryourlife.payments.tenant.domain.Tenant;
import com.foryourlife.shared.domain.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.sql.Date;

@Entity
public class Payment extends AggregateRoot {

    @Id
    private String id;
    private String lastDigits;
    private String clientTransactionId;
    private String transactionId;
    private String phoneNumber;
    private String email;
    private String cardType;
    private String transactionStatus;
    private String authorizationCode;
    private float amount;
    private Date paymentDate;
    @ManyToOne
    @JoinColumn(name = "planId", referencedColumnName = "id")
    private Plan plan;
    @ManyToOne
    @JoinColumn(name = "tenantId", referencedColumnName = "id")
    private Tenant tenant;

    protected Payment() {
    }

    private Payment(String id, String lastDigits, String clientTransactionId, String transactionId, String phoneNumber, String email, String cardType, String transactionStatus, String authorizationCode, float amount, Date paymentDate, Plan plan, Tenant tenant) {
        this.id = id;
        this.lastDigits = lastDigits;
        this.clientTransactionId = clientTransactionId;
        this.transactionId = transactionId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.cardType = cardType;
        this.transactionStatus = transactionStatus;
        this.authorizationCode = authorizationCode;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.plan = plan;
        this.tenant = tenant;
    }

    public static Payment create(String id, String lastDigits, String clientTransactionId, String transactionId, String phoneNumber, String email, String cardType, String transactionStatus, String authorizationCode, float amount, Date paymentDate, Plan plan, Tenant tenant) {
        var payment = new Payment(id, lastDigits, clientTransactionId, transactionId, phoneNumber, email, cardType, transactionStatus, authorizationCode, amount, paymentDate, plan, tenant);
        return payment;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Plan getPlan() {
        return plan;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public float getAmount() {
        return amount;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getCardType() {
        return cardType;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getClientTransactionId() {
        return clientTransactionId;
    }

    public String getLastDigits() {
        return lastDigits;
    }

    public String getId() {
        return id;
    }
}
