package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

public class SaveAttendanceRequest {
    @NotNull
    private String id;
    private AttendanceStatus fridayAttendance;
    private AttendanceStatus saturdayAttendance;
    private AttendanceStatus sundayAttendance;
    @NotNull
    private CourseLevel stage;
    @NotNull
    private Users userId;
    @NotNull
    private Training trainingId;

    public Attendance toDomain() {
        return  Attendance.create(id, fridayAttendance, saturdayAttendance, sundayAttendance, stage, userId, trainingId);
    }

}
