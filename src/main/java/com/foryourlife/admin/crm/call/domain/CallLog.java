package com.foryourlife.admin.crm.call.domain;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_logs")
public class CallLog extends AuditableEntity {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "called_user_id",
            referencedColumnName = "id"
    )
    private User calledUser;
    @ManyToOne
    @JoinColumn(
            name = "called_by_user_id",
            referencedColumnName = "id"
    )
    private User calledBy;
    @Column(
            name = "call_start_time"
    )
    private LocalDateTime startTime;
    @Column(
            name = "call_end_time"
    )
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private CallType type;
    @Enumerated(EnumType.STRING)
    private CallStatus status;
    private String notes;

    protected CallLog() {

    }

    public CallLog(String id, User calledUser, User calledBy, LocalDateTime startTime, LocalDateTime endTime, CallType type, CallStatus status, String notes) {
        this.id = id;
        this.calledUser = calledUser;
        this.calledBy = calledBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = status;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCalledUser() {
        return calledUser;
    }

    public void setCalledUser(User calledUser) {
        this.calledUser = calledUser;
    }

    public User getCalledBy() {
        return calledBy;
    }

    public void setCalledBy(User calledBy) {
        this.calledBy = calledBy;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
}
