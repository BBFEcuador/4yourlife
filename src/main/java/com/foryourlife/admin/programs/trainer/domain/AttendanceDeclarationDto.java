package com.foryourlife.admin.programs.trainer.domain;

import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AttendanceDeclarationDto {
    private String userName;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus firstAttendance;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus secondAttendance;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus thirdAttendance;
    private Integer firstPromise;
    private Integer secondPromise;
    private Integer thirdPromise;
    private Integer achievedCount;
    private Integer paidCount;
}
