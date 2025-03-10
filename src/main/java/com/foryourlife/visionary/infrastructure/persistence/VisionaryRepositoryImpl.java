package com.foryourlife.visionary.infrastructure.persistence;

import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisionaryRepositoryImpl implements VisionaryRepository {
    private JpaVisionaryRepository repository;

    @Override
    public void save(Visionary visionary) {
        if (visionary.getId() == null) {
            visionary.setId(UUID.randomUUID().toString());
        }
        repository.save(visionary);
    }

    @Override
    public Optional<Visionary> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Visionary> findAll() {
        return repository.findAll();
    }
}
