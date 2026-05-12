package com.foryourlife.shared.domain.user.infrastructure;

import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneralUserRepositoryImpl implements UserRepository {

    private final JpaUserRepository repository;

    public GeneralUserRepositoryImpl(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAllByIds(List<String> ids) {
        return repository.findAllByIdIn(ids);
    }

    @Override
    public List<User> findAllById(List<String> senderIds) {
        return repository.findAllByIdIn(senderIds);
    }
}
