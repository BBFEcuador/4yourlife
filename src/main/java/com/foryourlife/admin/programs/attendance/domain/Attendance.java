package com.foryourlife.admin.programs.attendance.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "attendances")
public class Attendance {
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
    private Users userId;
    @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id")
    private Training trainingId;

    public Attendance() {
    }

    private Attendance(String id, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance, FylStage stage, Users userId, Training trainingId) {
        this.id = id;
        this.fridayAttendance = fridayAttendance;
        this.saturdayAttendance = saturdayAttendance;
        this.sundayAttendance = sundayAttendance;
        this.stage = stage;
        this.userId = userId;
        this.trainingId = trainingId;
    }

    public static Attendance create(String id, AttendanceStatus fridayAttendance, AttendanceStatus saturdayAttendance, AttendanceStatus sundayAttendance, FylStage stage, Users userId, Training trainingId) {
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

    public Users getUserId() {
        return userId;
    }

    public Training getTrainingId() {
        return trainingId;
    }
}
