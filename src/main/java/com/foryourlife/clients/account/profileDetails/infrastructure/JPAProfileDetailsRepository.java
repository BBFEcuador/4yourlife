package com.foryourlife.clients.account.profileDetails.infrastructure;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAProfileDetailsRepository extends JpaRepository<ProfileDetails,String> {
}
