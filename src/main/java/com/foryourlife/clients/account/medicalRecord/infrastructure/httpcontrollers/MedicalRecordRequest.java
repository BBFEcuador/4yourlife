package com.foryourlife.clients.account.medicalRecord.infrastructure.httpcontrollers;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.validation.constraints.NotNull;

public class MedicalRecordRequest {
    @NotNull
    public String id;

    public String  psychiatric_history_detail;

    public String  medical_history_detail;

    public String  medication_history_detail;

    public Participant participant;

    public MedicalRecordRequest(String id, String psychiatric_history_detail, String medical_history_detail, String medication_history_detail, Participant participant) {
        this.id = id;
        this.psychiatric_history_detail = psychiatric_history_detail;
        this.medical_history_detail = medical_history_detail;
        this.medication_history_detail = medication_history_detail;
        this.participant = participant;
    }

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

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public MedicalRecord toDomain(){
        return MedicalRecord.create(id != null ? id: java.util.UUID.randomUUID().toString(), this.psychiatric_history_detail, this.medical_history_detail, this.medication_history_detail, this.participant);
    }

}
