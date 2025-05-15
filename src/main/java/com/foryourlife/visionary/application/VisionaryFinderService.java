package com.foryourlife.visionary.application;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisionaryFinderService {
    private final VisionaryRepository repository;

    public VisionaryFinderService(VisionaryRepository repository) {
        this.repository = repository;
    }

    public Optional<Visionary> getById(String id) {
        return repository.findById(id);
    }

    public List<Visionary> getAll(){
        return repository.findAll();
    }
    public Page<Visionary> getAll(Pageable pageable){
        return repository.findAll(pageable);
    }
    public Page<Visionary> getAll(Pageable pageable, Criteria criteria){
        return repository.findAll(pageable,criteria);
    }

    public List<Visionary> match(Criteria criteria){
        return repository.match(criteria);
    }
    public List<Visionary> findAvailableVisionaries(LocalDate startDate,LocalDate endDate){
        return repository.findAvailableVisionaries(startDate,endDate);
    }
}
