package com.foryourlife.clients.account.medicalRecord.domain;

import java.util.Optional;

public interface MedicalRecordRepository {
    void save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findByParticipantId(String userId);
}
