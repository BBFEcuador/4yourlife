package com.foryourlife.admin.training.campus.application;

import com.foryourlife.admin.training.campus.domain.Campus;
import com.foryourlife.admin.training.campus.domain.CampusNotFoundException;
import com.foryourlife.admin.training.campus.domain.CampusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandCampusService {
    private final CampusRepository _campusRepository;
    private final Logger logger = LoggerFactory.getLogger(CommandCampusService.class);


    public CommandCampusService(CampusRepository _campusRepository) {
        this._campusRepository = _campusRepository;
    }

    public void save(Campus campus) {
        try {
            this._campusRepository.save(campus);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public void update(Campus campus) {
        if (!this._campusRepository.findById(campus.getId()).isPresent())
            throw new CampusNotFoundException("The Id: " + campus.getId() + " doesn't exist.");
        try {
            this._campusRepository.save(campus);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
}
