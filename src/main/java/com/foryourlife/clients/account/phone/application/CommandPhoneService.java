package com.foryourlife.clients.account.phone.application;

import com.foryourlife.clients.account.phone.domain.Phone;
import com.foryourlife.clients.account.phone.domain.PhoneRepository;
import org.springframework.stereotype.Service;

@Service
public class CommandPhoneService {
    private final PhoneRepository _phoneRepository;

    public CommandPhoneService(PhoneRepository _phoneRepository) {
        this._phoneRepository = _phoneRepository;
    }

    public void save(Phone phone) {
        _phoneRepository.save(phone);
    }

    public void delete(String id) {
        _phoneRepository.deleteById(id);
    }
}
