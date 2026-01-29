package com.foryourlife.clients.account.medicalRecord.domain;

import org.apache.poi.sl.draw.geom.GuideIf;

import java.util.Optional;

public interface MedicalRecordRepository {
    void save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findByParticipantId(String userId);
    Optional<MedicalRecord> findByMedicalRecordId(String medicalRecordId);
}
