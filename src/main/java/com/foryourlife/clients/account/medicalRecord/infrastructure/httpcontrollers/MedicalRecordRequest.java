package com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.validation.constraints.NotNull;

public class MedicalRecordRequest {
    @NotNull
    public String id;
    @NotNull
    public String  psychiatric_history_detail;
    @NotNull
    public String  medical_history_detail;
    @NotNull
    public String  medication_history_detail;
    @NotNull
    public String participantId;

    public void setId(String id) {
        this.id = id;
    }

    public void setPsychiatric_history_detail(String psychiatric_history_detail) {
        this.psychiatric_history_detail = psychiatric_history_detail;
    }

    public void setMedical_history_detail(String medical_history_detail) {
        this.medical_history_detail = medical_history_detail;
    }

    public void setMedication_history_detail(String medication_history_detail) {
        this.medication_history_detail = medication_history_detail;
    }

}
