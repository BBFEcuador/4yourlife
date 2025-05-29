package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

public class MedicalRecordSaveRequest {
    @NotNull
    public String  psychiatric_history_detail;
    @NotNull
    public String  medical_history_detail;
    @NotNull
    public String  medication_history_detail;

    public MedicalRecordSaveRequest(String psychiatric_history_detail, String medical_history_detail, String medication_history_detail) {
        this.psychiatric_history_detail = psychiatric_history_detail;
        this.medical_history_detail = medical_history_detail;
        this.medication_history_detail = medication_history_detail;
    }
}
