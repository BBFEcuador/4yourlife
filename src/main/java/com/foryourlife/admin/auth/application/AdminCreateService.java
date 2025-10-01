package com.foryourlife.admin.auth.application;


import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import com.foryourlife.admin.auth.infrastructure.httpControllers.CreateAdminRequest;
import com.foryourlife.admin.auth.infrastructure.httpControllers.DisableAdminRequest;
import com.foryourlife.admin.auth.infrastructure.httpControllers.UpdateAdminRequest;
import com.foryourlife.admin.auth.infrastructure.httpControllers.UpdatePassAdminRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdminCreateService {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CommandGeneralUserService commandGeneralUserService;
    private final AdminRoleRepository roleFinderService;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(AdminCreateService.class);

    public AdminCreateService(AdminRepository repository, PasswordEncoder passwordEncoder, UserRepository userRepository, CommandGeneralUserService commandGeneralUserService, AdminRoleRepository roleFinderService, EventBus bus) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.commandGeneralUserService = commandGeneralUserService;
        this.roleFinderService = roleFinderService;
        this.bus = bus;
    }
    @Transactional
    public void create(CreateAdminRequest admin) {
        var adminId = admin.id != null ? admin.id : UUID.randomUUID().toString();
        var user = new User(
                UUID.randomUUID().toString(),
                admin.email,
                passwordEncoder.encode(admin.password),
                admin.name1,
                admin.name2,
                admin.lastName1,
                admin.lastName2,
                admin.name1 + " " + admin.name2 + " " + admin.lastName1 + " " + admin.lastName2,
                admin.phone,
                List.of(new UserEntities(adminId, UserType.ADMIN.toString()))
        );
        commandGeneralUserService.save(user);
        var newAdmin = Admin.create(admin.id != null ? admin.id : UUID.randomUUID().toString(), user, admin.role, admin.campus, admin.password);
        this.repository.save(newAdmin);
    }

    public void update(UpdateAdminRequest request) {
        var admin = this.repository.findById(request.getId()).orElseThrow(() -> new BaseException("Admin not found", List.of()));
        repository.save(admin);
    }

    public void updateState(DisableAdminRequest adminReq) {
        var admin = this.repository.findById(adminReq.getId()).orElseThrow(() -> new BaseException("Admin not found", List.of()));
        admin.setActive(adminReq.isActive());
        repository.save(admin);
    }

    public void updatePass(UpdatePassAdminRequest adminReq) {
        var admin = this.repository.findById(adminReq.id).orElseThrow(() -> new BaseException("Admin not found", List.of()));
        repository.save(admin);
    }

    public void changeRole(String id, String roleId) {
        var admin = this.repository.findById(id).orElseThrow(() -> new BaseException("Admin not found", List.of()));
        var role = this.roleFinderService.findById(roleId).orElseThrow(() -> new BaseException("Role not found", List.of()));
        admin.setRole(role);
        repository.save(admin);
    }

}
