package com.foryourlife.clients.account.invoiceData.domain;

import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "data_invoices")
public class DataInvoice {
    @Id
    private String id;
    private String fullName;
    private String address;
    private String document;
    private String phone;
    private String email;
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;

    protected DataInvoice() {
    }

    public DataInvoice(String id, String fullName, String address, String document, String phone, String email, User user) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static DataInvoice create(String id, String fullName, String address, String document, String phone, String email, User user){
        return new DataInvoice(id, fullName, address, document, phone, email, user);
    }
}

