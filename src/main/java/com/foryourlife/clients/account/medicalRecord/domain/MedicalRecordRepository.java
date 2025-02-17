package com.foryourlife.clients.account.medicalRecord.domain;

public interface MedicalRecordRepository {
    void save(MedicalRecord medicalRecord);
    MedicalRecord findByParticipantId(String userId);
}
