package com.foryourlife.visionary.application;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.stereotype.Service;

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

    public List<Visionary> match(Criteria criteria){
        return repository.match(criteria);
    }
}
