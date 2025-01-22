package com.foryourlife.admin.auth.application;

import com.foryourlife.account.user.domain.UserAlreadyCreatedException;
import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminCreateService {

    private final AdminRepository repository;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(AdminCreateService.class);

    public AdminCreateService(AdminRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    public void save(Admin admin){
        if(this.repository.findByEmail(admin.getEmail()).isPresent()){
            throw new UserAlreadyCreatedException("The email " + admin.getEmail() + "is already registered");
        }
        try{
            this.repository.save(admin);
        }catch (Exception e){
            this.logger.error(e.getMessage(), e);
        }
    }

    public void update(Admin admin){
        try{
            this.repository.save(admin);
        }catch (Exception e){
            this.logger.error(e.getMessage(), e);
        }
    }

}
