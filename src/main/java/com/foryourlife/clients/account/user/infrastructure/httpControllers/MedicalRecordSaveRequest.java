package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

public class MedicalRecordSaveRequest {
    @NotNull
    public String  psychiatric_history_detail;
    @NotNull
    public String  medical_history_detail;
    @NotNull
    public String  medication_history_detail;
}
