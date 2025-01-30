package com.foryourlife.clients.account.phone.infrastructure;

import com.foryourlife.clients.account.phone.domain.Phone;
import com.foryourlife.clients.account.phone.domain.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneRepositoryImpl implements PhoneRepository {
    private final JPAPhoneRepository _jpaPhoneRepository;

    public PhoneRepositoryImpl(JPAPhoneRepository _jpaPhoneRepository) {
        this._jpaPhoneRepository = _jpaPhoneRepository;
    }

    @Override
    public void save(Phone phone) {
        _jpaPhoneRepository.save(phone);
    }

    @Override
    public void deleteById(String id) {
        _jpaPhoneRepository.deleteById(id);
    }

    @Override
    public List<Phone> findAllByUser(String userId) {
        return _jpaPhoneRepository.findByUser_id(userId);
    }

    @Override
    public Optional<Phone> findById(String id) {
        return _jpaPhoneRepository.findById(id);
    }
}
