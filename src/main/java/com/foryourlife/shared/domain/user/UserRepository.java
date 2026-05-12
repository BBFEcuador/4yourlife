package com.foryourlife.shared.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save (User user);
    Optional<User> findByEmail (String email);
    Optional<User> findById (String id);
    List<User> findAllByIds (List<String> ids);
    List<User> findAllById(List<String> senderIds);
}
