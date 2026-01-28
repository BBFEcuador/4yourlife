package com.foryourlife.clients.account.medicalRecord.infrastructure.persistence;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAMedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    Optional<MedicalRecord> findByParticipant_Id(String participantId);
}
