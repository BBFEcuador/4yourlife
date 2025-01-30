package com.foryourlife.clients.account.profileDetails.application;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void updateProfile(ProfileDetails profileDetails,String id) {
        try {
            ensureProfileExist(id);
            this.repository.save(profileDetails);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void ensureProfileExist(String id){
        this.repository.findById(id).orElseThrow(() ->
                    new BaseException("Not Found", List.of("Profile not exist"))
                );
    }
}
