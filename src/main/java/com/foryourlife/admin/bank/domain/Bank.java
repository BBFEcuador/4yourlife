package com.foryourlife.admin.bank.domain;

import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.persistence.*;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    private String id;
    private String name;
    private String number;
    private String contificoId;
    @ManyToOne
    @JoinColumn(
            name = "campus_id",
            referencedColumnName = "id"
    )
    private Campus campus;

    protected Bank() {}

    public Bank(String id, String name, String number, String contificoId, Campus campus) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.contificoId = contificoId;
        this.campus = campus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContificoId() {
        return contificoId;
    }

    public void setContificoId(String contificoId) {
        this.contificoId = contificoId;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
