package com.foryourlife.admin.programs.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.OnNullDesistedAttend;
import com.foryourlife.shared.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "attendances")
public class Attendance extends AggregateRoot {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus fridayAttendance;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus saturdayAttendance;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus sundayAttendance;
    @Enumerated(EnumType.STRING)
    private FylStage stage;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"user", "participantLevel", "campus", "modules", "contacts", "medicalRecord"})
    private User user;
    @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"nextLevel", "campus", "originalTeam","courseLevelDisplay"})
    private Training training;
    @Column(name = "is_active")
    @JsonProperty("isActive")
    private Boolean isActive = true;

    public Attendance() {
    }

    private Attendance(String id, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance, FylStage stage, User userId, Training trainingId) {
        this.id = id;
        this.fridayAttendance = fridayAttendance;
        this.saturdayAttendance = saturdayAttendance;
        this.sundayAttendance = sundayAttendance;
        this.stage = stage;
        this.user = userId;
        this.training = trainingId;
    }

    public static Attendance create(String id, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance, FylStage stage, User userId, Training trainingId) {
        return new Attendance(id, fridayAttendance, saturdayAttendance, sundayAttendance, stage, userId, trainingId);
    }

    public String getId() {
        return id;
    }

    public AttendanceStatus getFridayAttendance() {
        return fridayAttendance;
    }

    public AttendanceStatus getSaturdayAttendance() {
        return saturdayAttendance;
    }

    public AttendanceStatus getSundayAttendance() {
        return sundayAttendance;
    }

    public FylStage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public Training getTraining() {
        return training;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setFridayAttendance(AttendanceStatus fridayAttendance) {
        this.fridayAttendance = fridayAttendance;
    }

    public void setSaturdayAttendance(AttendanceStatus saturdayAttendance) {
        this.saturdayAttendance = saturdayAttendance;
    }

    public void setSundayAttendance(AttendanceStatus sundayAttendance) {
        this.sundayAttendance = sundayAttendance;
    }

    public boolean HasUnAttendance() {
        var unAttendance = this.fridayAttendance == AttendanceStatus.NO_ASISTIO || this.fridayAttendance == AttendanceStatus.DESERTO || this.saturdayAttendance == AttendanceStatus.NO_ASISTIO || this.saturdayAttendance == AttendanceStatus.DESERTO || this.sundayAttendance == AttendanceStatus.NO_ASISTIO || this.sundayAttendance == AttendanceStatus.DESERTO;
//        if (unAttendance) {
//            this.record(new OnNullDesistedAttend(this.user.getId(), this.user, this.training, this));
//        }
        return unAttendance;
    }
}
