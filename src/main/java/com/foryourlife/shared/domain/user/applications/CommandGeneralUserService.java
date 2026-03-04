package com.foryourlife.shared.domain.user.applications;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.infrastructure.MutateUserRequest;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import com.foryourlife.visionary.infrastructure.httpControllers.VisionaryController;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandGeneralUserService {
    private final UserRepository repository;
    private final StaffRepository staffRepository;
    private final VisionaryRepository visionaryRepository;
    private final MasterLifeRepository masterLifeRepository;

    public CommandGeneralUserService(UserRepository repository, StaffRepository staffRepository, VisionaryRepository visionaryRepository, MasterLifeRepository masterLifeRepository) {
        this.repository = repository;
        this.staffRepository = staffRepository;
        this.visionaryRepository = visionaryRepository;
        this.masterLifeRepository = masterLifeRepository;
    }

    public void save(User user) {
        repository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new BaseException("Usuario ya registrado", List.of("El email " + user.getEmail() + " ya fue registrado"));
        });
        repository.save(user);
    }


    public void mutate(MutateUserRequest req) {
        var user = repository.findById(req.id).orElseThrow(() ->
                new BaseException("Error", List.of("Usuario no encontrado"))
        );
        var list = user.getEntityMap();
        var id = UUID.randomUUID().toString();
        switch (req.type) {
            case "ML":
                masterLifeRepository.findByUserId(req.id).ifPresent(masterLife -> {
                    throw new BaseException("Error", List.of("Ya esta creado como masterlife"));
                });
                var masterLife = new MasterLife(
                        id,
                        true,
                        user
                );

                list.add(new UserEntities(id, UserType.MASTER_LIFE.toString()));
                user.setEntityMap(list);
                masterLifeRepository.save(masterLife);
                break;
            case "V":
                visionaryRepository.findByUserId(req.id).ifPresent(v -> {
                    throw new BaseException("Error", List.of("Ya esta creado como Visionario"));
                });
                var visionary = Visionary.create(
                        id,
                        true,
                        user
                );
                list.add(new UserEntities(id, UserType.VISIONARY.toString()));
                user.setEntityMap(list);
                visionaryRepository.save(visionary);
                break;
            case "S":
                staffRepository.findByUserId(req.id).ifPresent(s -> {
                    throw new BaseException("Error", List.of("Ya esta creado como Staff"));
                });
                var staff = Staff.create(
                        id,
                        "CAPITAN",
                        true,
                        user
                );
                list.add(new UserEntities(id, UserType.STAFF.toString()));
                user.setEntityMap(list);
                staffRepository.save(staff);
                break;
            default:
                throw new BaseException("Error", List.of("Tipo de usuario no admitido"));
        }
        repository.save(user);
    }
}
