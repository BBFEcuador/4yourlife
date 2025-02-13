package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.validation.constraints.NotNull;

public class SaveAttendanceRequest {
    @NotNull
    private String id;
    private AttendanceStatus fridayAttendance;
    private AttendanceStatus saturdayAttendance;
    private AttendanceStatus sundayAttendance;
    @NotNull
    private FylStage stage;
    @NotNull
    private Participant userId;
    @NotNull
    private Training trainingId;

    public Attendance toDomain() {
        return  Attendance.create(id, fridayAttendance, saturdayAttendance, sundayAttendance, stage, userId, trainingId);
    }

}
