package com.foryourlife.admin.programs.campus.application;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryCampusService {
    private final CampusRepository _campusRepository;
    private final Logger logger = LoggerFactory.getLogger(QueryCampusService.class);

    public QueryCampusService(CampusRepository _campusRepository) {
        this._campusRepository = _campusRepository;
    }

    public List<Campus> getAll() {
        return this._campusRepository.getAll();
    }

    public Campus findById(String id) {
        return this._campusRepository.findById(id)
                .orElseThrow(() ->
                    new BaseException("Not found",List.of("The campus with id "+id+" does not exist"))
                );
    }
}
