package com.foryourlife.visionary.application;

import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisionaryFinderService {
    private static VisionaryRepository repository;

    public VisionaryFinderService(VisionaryRepository repository) {
        this.repository = repository;
    }

    public Optional<Visionary> getById(String id) {
        return repository.findById(id);
    }

    public List<Visionary> getAll(){
        return repository.findAll();
    }
    public List<Visionary> findAvailableVisionaries(LocalDate startDate,LocalDate endDate){
        return repository.findAvailableVisionaries(startDate,endDate);
    }
}
