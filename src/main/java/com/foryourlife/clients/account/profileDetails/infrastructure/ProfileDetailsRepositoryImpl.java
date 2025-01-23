package com.foryourlife.clients.account.profileDetails.infrastructure;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileDetailsRepositoryImpl implements ProfileDetailsRepository {

    private final JPAProfileDetailsRepository repository;

    public ProfileDetailsRepositoryImpl(JPAProfileDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ProfileDetails profileDetails) {
        this.repository.save(profileDetails);
    }
}
