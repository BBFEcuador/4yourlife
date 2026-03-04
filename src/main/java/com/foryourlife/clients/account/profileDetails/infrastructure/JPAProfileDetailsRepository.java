package com.foryourlife.clients.account.profileDetails.infrastructure;

import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAProfileDetailsRepository extends JpaRepository<ProfileDetails,String> {
     Optional<ProfileDetails> findByDni(String dni);
}
