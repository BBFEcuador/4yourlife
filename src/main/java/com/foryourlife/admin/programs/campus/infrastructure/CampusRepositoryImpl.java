package com.foryourlife.admin.programs.campus.infrastructure;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampusRepositoryImpl implements CampusRepository {

    private final JPACampusRepository _jpaCampusRepository;

    public CampusRepositoryImpl(JPACampusRepository _jpaCampusRepository) {
        this._jpaCampusRepository = _jpaCampusRepository;
    }

    @Override
    public Optional<Campus> findById(String id) {
        return _jpaCampusRepository.findById(id);
    }

    @Override
    public List<Campus> getAll() {
        return _jpaCampusRepository.findAll();
    }

    @Override
    public void update(Campus campus) {
        _jpaCampusRepository.save(campus);
    }

    @Override
    public void save(Campus campus) {
        _jpaCampusRepository.save(campus);
    }
}
