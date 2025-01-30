package com.foryourlife.clients.account.profileDetails.domain;

import java.util.Optional;

public interface ProfileDetailsRepository {

    void save(ProfileDetails profileDetails);

    Optional<ProfileDetails> findById(String id);
}
