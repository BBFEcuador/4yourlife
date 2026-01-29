package com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers;

import jakarta.validation.constraints.NotNull;

public class MedicalRecordUpdateRequest {
    @NotNull
    public String  psychiatric_history_detail;
    @NotNull
    public String  medical_history_detail;
    @NotNull
    public String  medication_history_detail;

    public String getPsychiatric_history_detail() {
        return psychiatric_history_detail;
    }

    public void setPsychiatric_history_detail(String psychiatric_history_detail) {
        this.psychiatric_history_detail = psychiatric_history_detail;
    }

    public String getMedical_history_detail() {
        return medical_history_detail;
    }

    public void setMedical_history_detail(String medical_history_detail) {
        this.medical_history_detail = medical_history_detail;
    }

    public String getMedication_history_detail() {
        return medication_history_detail;
    }

    public void setMedication_history_detail(String medication_history_detail) {
        this.medication_history_detail = medication_history_detail;
    }
}
