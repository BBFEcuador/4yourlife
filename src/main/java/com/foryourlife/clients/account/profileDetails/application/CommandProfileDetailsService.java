package com.foryourlife.clients.account.profileDetails.application;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandProfileDetailsService {

    private final ProfileDetailsRepository repository;
    private final Logger logger = LoggerFactory.getLogger(CommandProfileDetailsService.class);

    public CommandProfileDetailsService(ProfileDetailsRepository repository) {
        this.repository = repository;
    }

    public void addProfile(ProfileDetails profileDetails) {
        try {
            this.repository.save(profileDetails);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
