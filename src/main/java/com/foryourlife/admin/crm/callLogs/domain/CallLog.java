package com.foryourlife.admin.crm.callLogs.domain;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_logs")
public class CallLog extends AuditableEntity {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "called_by_user_id",
            referencedColumnName = "id"
    )
    private User calledBy;
    @Column(
            name = "date"
    )
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private CallType type;
    @Enumerated(EnumType.STRING)
    private CallStatus status;
    private String notes;
    @ManyToOne
    @JoinColumn(
            name = "call_id",
            referencedColumnName = "id"
    )
    private Call call;

    protected CallLog() {

    }

    public CallLog(String id, User calledBy, LocalDate date, CallType type, CallStatus status, String notes, Call call) {
        this.id = id;
        this.calledBy = calledBy;
        this.date = date;
        this.type = type;
        this.status = status;
        this.notes = notes;
        this.call = call;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCalledBy() {
        return calledBy;
    }

    public void setCalledBy(User calledBy) {
        this.calledBy = calledBy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CallType getType() {
        return type;
    }

    public void setType(CallType type) {
        this.type = type;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
