package com.foryourlife.admin.training.campus.application;

import com.foryourlife.admin.training.campus.domain.Campus;
import com.foryourlife.admin.training.campus.domain.CampusRepository;
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

    public List<Campus> getAll(){
        return this._campusRepository.getAll();
    }

    public Campus findById(String id){
        return this._campusRepository.findById(id).orElseThrow();
    }
}
