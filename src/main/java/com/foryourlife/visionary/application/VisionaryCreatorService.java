package com.foryourlife.visionary.application;

import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.stereotype.Service;

@Service
public class VisionaryCreatorService {
    private static VisionaryRepository repository;

    public VisionaryCreatorService(VisionaryRepository repository) {
        this.repository = repository;
    }

    public void create(Visionary visionary) {
        repository.save(visionary);
    }

    public void delete(String visionaryId){
        repository.deleteById(visionaryId);
    }
}
