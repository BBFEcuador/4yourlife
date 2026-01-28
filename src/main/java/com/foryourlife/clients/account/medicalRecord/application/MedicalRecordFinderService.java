package com.foryourlife.clients.account.medicalRecord.application;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordFinderService {
    private final MedicalRecordRepository repository;

    public MedicalRecordFinderService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public Optional<MedicalRecord> findByParticipant(String userId) {
        return repository.findByParticipantId(userId);
    }
}
