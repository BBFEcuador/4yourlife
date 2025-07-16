package com.foryourlife.admin.contifico.config.domain;

import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.persistence.*;

@Entity
@Table(name = "configs_contifico")
public class ConfigContifico {
    @Id
    private String id;
    private String apiKey;
    private String apiSecret;
    @Column(
            name = "razon_social"
    )
    private String razonSocial;
    private String address;
    private String phone;
    private String ruc;
    @OneToOne
    @JoinColumn(
            name = "campus_id",
            referencedColumnName = "id"
    )
    private Campus campus;

    protected ConfigContifico() {
    }

    public ConfigContifico(String id, String apiKey, String apiSecret, String razonSocial, String address, String phone, String ruc, Campus campus) {
        this.id = id;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.razonSocial = razonSocial;
        this.address = address;
        this.phone = phone;
        this.ruc = ruc;
        this.campus = campus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
