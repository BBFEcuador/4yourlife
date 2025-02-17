package com.foryourlife.clients.account.medicalRecord.application;

import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordCreatorService {
    private final MedicalRecordRepository repository;

    public MedicalRecordCreatorService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public void createMedicalRecord(MedicalRecord medicalRecord) {
        repository.save(medicalRecord);
    }
}
