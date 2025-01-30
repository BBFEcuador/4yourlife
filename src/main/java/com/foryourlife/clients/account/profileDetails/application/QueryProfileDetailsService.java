package com.foryourlife.clients.account.profileDetails.application;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryProfileDetailsService {
    private final ProfileDetailsRepository repository;
    private final Logger logger = LoggerFactory.getLogger(CommandProfileDetailsService.class);

    public QueryProfileDetailsService(ProfileDetailsRepository repository) {
        this.repository = repository;
    }

    public ProfileDetails findById(String id){
        return this.repository.findById(id).orElseThrow(() ->
                new BaseException("Not Found", List.of("Profile not exist"))
        );
    }
}
