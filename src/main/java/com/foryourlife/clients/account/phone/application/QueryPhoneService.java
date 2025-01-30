package com.foryourlife.clients.account.phone.application;

import com.foryourlife.clients.account.phone.domain.Phone;
import com.foryourlife.clients.account.phone.domain.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryPhoneService {
    private final PhoneRepository _phoneRepository;

    public QueryPhoneService(PhoneRepository _phoneRepository) {
        this._phoneRepository = _phoneRepository;
    }

    public Optional<Phone> findById(String id) {
        return _phoneRepository.findById(id);
    }

    public List<Phone> findAllByUser(String userId) {
        return _phoneRepository.findAllByUser(userId);
    }
}
