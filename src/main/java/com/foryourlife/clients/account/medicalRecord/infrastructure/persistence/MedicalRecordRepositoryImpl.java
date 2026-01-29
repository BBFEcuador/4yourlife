package com.foryourlife.clients.account.medicalRecord.infrastructure.persistence;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private final JPAMedicalRecordRepository repository;

    public MedicalRecordRepositoryImpl(JPAMedicalRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        this.repository.save(medicalRecord);
    }

    @Override
    public Optional<MedicalRecord> findByParticipantId(String userId) {
        return this.repository.findByParticipant_Id(userId);
    }

    @Override
    public Optional<MedicalRecord> findByMedicalRecordId(String medicalRecordId) {
        return this.repository.findById(medicalRecordId);
    }
}
