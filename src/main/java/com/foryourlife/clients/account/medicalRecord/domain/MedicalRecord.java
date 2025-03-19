package com.foryourlife.clients.account.medicalRecord.domain;

import com.foryourlife.clients.account.user.domain.Participant;
import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    public String id;

    @Column(columnDefinition = "varchar(255) default 'N/A'")
    public String psychiatric_history_detail;

    @Column(columnDefinition = "varchar(255) default 'N/A'")
    public String medical_history_detail;

    @Column(columnDefinition = "varchar(255) default 'N/A'")
    public String medication_history_detail;

    @OneToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    public Participant participant;

    protected MedicalRecord() {
    }

    public MedicalRecord(String id, String psychiatric_history_detail, String medical_history_detail, String medication_history_detail, Participant participant) {
        this.id = id;
        this.psychiatric_history_detail = psychiatric_history_detail;
        this.medical_history_detail = medical_history_detail;
        this.medication_history_detail = medication_history_detail;
        this.participant = participant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public static MedicalRecord create(String id, String psychiatric_history_detail, String medical_history_detail, String medication_history_detail, Participant participant) {
        return new MedicalRecord(id, psychiatric_history_detail, medical_history_detail, medication_history_detail, participant);
    }
}
