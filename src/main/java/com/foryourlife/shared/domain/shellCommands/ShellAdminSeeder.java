package com.foryourlife.shared.domain.shellCommands;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import com.foryourlife.admin.permission.domain.Permission;
import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.programs.trainer.application.TrainerCreatorService;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import com.foryourlife.admin.programs.training.infrastructure.httpControllers.TrainingAutoGenerateRequest;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.programs.domain.ProgramRepository;
import com.foryourlife.clients.account.contact.infrastructure.httpControllers.SaveContactRequest;
import com.foryourlife.clients.account.invitations.applications.CommandInvitationService;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.invitations.infrastructure.InvitationRequest;
import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.infrastructure.httpControllers.MedicalRecordSaveRequest;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.profileDetails.infrastructure.ProfileDetailRequest;
import com.foryourlife.masterLife.application.CommandMasterLifeService;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.staff.application.StaffCreatorService;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.application.VisionaryCreatorService;
import com.foryourlife.visionary.domain.Visionary;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@ShellComponent
@Service
public class ShellAdminSeeder {
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ProgramRepository programRepository;
    private final CampusRepository campusRepository;
    private final TrainerCreatorService trainerCreatorService;
    private final CommandTrainingService commandTrainingService;
    private final StaffCreatorService staffCreatorService;
    private final VisionaryCreatorService visionaryCreatorService;
    private final ParticipantCommandService participantCommandService;
    private final CommandMasterLifeService commandMasterLifeService;
    private final AdminRoleRepository adminRoleRepository;
    private final CommandInvitationService commandInvitationService;
    private final QueryInvitationServices queryInvitationServices;
    private final ParticipantLevelRepository participantLevelRepository;
    private final Faker faker = new Faker();
    private final RolesAndPermissionSeederShell rolesAndPermissionSeederShell;

    public ShellAdminSeeder(AdminRepository repository, PasswordEncoder passwordEncoder, ProgramRepository programRepository, CampusRepository campusRepository, TrainerCreatorService trainerCreatorService, CommandTrainingService commandTrainingService, StaffCreatorService staffCreatorService, VisionaryCreatorService visionaryCreatorService, ParticipantCommandService participantCommandService, CommandMasterLifeService commandMasterLifeService, AdminRoleRepository adminRoleRepository, CommandInvitationService commandInvitationService, QueryInvitationServices queryInvitationServices, ParticipantLevelRepository participantLevelRepository, RolesAndPermissionSeederShell rolesAndPermissionSeederShell) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.programRepository = programRepository;
        this.campusRepository = campusRepository;
        this.trainerCreatorService = trainerCreatorService;
        this.commandTrainingService = commandTrainingService;
        this.staffCreatorService = staffCreatorService;
        this.visionaryCreatorService = visionaryCreatorService;
        this.participantCommandService = participantCommandService;
        this.commandMasterLifeService = commandMasterLifeService;
        this.adminRoleRepository = adminRoleRepository;
        this.commandInvitationService = commandInvitationService;
        this.queryInvitationServices = queryInvitationServices;
        this.participantLevelRepository = participantLevelRepository;
        this.rolesAndPermissionSeederShell = rolesAndPermissionSeederShell;
    }

    @ShellMethod(key = "app:seed-admins")
     public void seedAdmins() {
        rolesAndPermissionSeederShell.seedRoles();
        List<Campus> campuses = List.of(
                Campus.create("61d88b2a-a22e-4cb0-8e43-e036483039d6", "Ecuador", "Quito", "De los Cedros OE1-13 y Real Audiencia", "094456123"),
                Campus.create("01a680a4-3bf6-4caf-98b8-f3d5c7a8811b", "Ecuador", "Guayaquil", "Alborada etapa 14 manzana 5", "094456123"),
                Campus.create("a35d480e-f17a-4b5c-887e-2ba9ddd3b696", "Ecuador", "Cuenca", "Por definir", "094456123")
        );

        campuses.forEach(campus -> {
            if (campusRepository.findById(campus.getId()).isEmpty()) {
                campusRepository.save(campus);
            }
        });

        repository.save(
                new Admin(
                        "3936ae5e-0cc1-4375-abc7-520d16999110",
                        new User(
                                "72efe963-52b5-439a-ada2-3ea7b0258b89",
                                "diegofyl@admin.com",
                                passwordEncoder.encode("FocusYourLife2025--"),
                                "DIEGO",
                                "",
                                "FYL",
                                "",
                                "Diego FYL",
                                "0999999999",
                                List.of(
                                        new UserEntities(
                                                "3936ae5e-0cc1-4375-abc7-520d16999110",
                                                "ADMIN")
                                )
                        ),
                        adminRoleRepository.findById("f4dddf05-8fec-4551-8d93-d6309c17c206").orElseThrow(),
                        new HashSet<>(campusRepository.getAll()), true)
        );
        repository.save(
                new Admin(
                        "1a34e4df-33fa-4078-9acd-df7d04dde5f4",
                        new User("93030dda-f43f-44bb-a500-991cda675aa7",
                                "jairo@admin.com",
                                passwordEncoder.encode("FocusYourLife2025--"),
                                "JAIRO",
                                "",
                                "FYL",
                                "",
                                "Jairo FYL",
                                "0999999999",
                                List.of(
                                        new UserEntities(
                                                "3936ae5e-0cc1-4375-abc7-520d16999110",
                                                "ADMIN")
                                )
                        ),
                        adminRoleRepository.findById("f4dddf05-8fec-4551-8d93-d6309c17c206").orElseThrow(),
                        new HashSet<>(campusRepository.getAll()), true));
        repository.save(
                new Admin(
                        "b85430d1-e9fc-4c8d-8dd0-6c483511f0b8",
                        new User(
                                "4f158ff7-3584-4c1b-abce-d1cbee0ced26",
                                "coordinacion@admin.com",
                                passwordEncoder.encode("FocusYourLife2025--"),
                                "COORDINACION",
                                "",
                                "FYL",
                                "",
                                "Coordinacion FYL",
                                "0999999999",
                                List.of(
                                        new UserEntities(
                                                "3936ae5e-0cc1-4375-abc7-520d16999110",
                                                "ADMIN"
                                        )
                                )
                        ),
                        adminRoleRepository.findById("f4dddf05-8fec-4551-8d93-d6309c17c206").orElseThrow(),
                        new HashSet<>(List.of(campuses.get(2))), true)
        );

        List<ParticipantLevel> roles = Arrays.asList(
                ParticipantLevel.create("6642e863-7f6f-40a3-80e2-934388ade735", "ROLE_INIT", true, CourseLevel.INIT),
                ParticipantLevel.create("3024c8f1-d603-47fc-8369-0e90cd2e703e", "ROLE_FOCUS", false, CourseLevel.FOCUS),
                ParticipantLevel.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0", "ROLE_YOUR", false, CourseLevel.YOUR),
                ParticipantLevel.create("5b2da953-9791-47e6-a5b8-3442b52b8ebc", "ROLE_LIFE", false, CourseLevel.LIFE),
                ParticipantLevel.create("797eb700-4a0c-4334-a9c0-5eb5de18b1b9", "ROLE_GRADUATE", false, CourseLevel.LIFE_GRADUATE)
        );

        this.participantLevelRepository.saveAll(roles);

//        createAdminTestUser();

        List<Program> programs = Arrays.asList(
                Program.create("55c3da1c-b516-4a55-9fdd-21317ee6e4c0", "Focus", CourseLevel.FOCUS),
                Program.create("bff32809-d719-4dfd-90b0-6f7a0f63e2fe", "Your", CourseLevel.YOUR),
                Program.create("3024c8f1-d603-47fc-8369-0e90cd2e703e", "Life", CourseLevel.LIFE)
        );

        this.programRepository.saveAll(programs);
    }

    private void createAdminTestUser() {
        for (int i = 1; i < 21; i++) {
            var email = "adminuser" + i + "@admin.com";
            var testUser = repository.findByEmail(email);
            if (testUser.isEmpty()) {
                repository.save(
                        new Admin(UUID.randomUUID().toString(), new User(UUID.randomUUID().toString(), email, passwordEncoder.encode("FocusYourLife2025--"), "Usuario", "", "Test", "", "Usuario test" + i, "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "ADMIN"))),
                                adminRoleRepository.findById("c1b2a3d4-e5f6-7890-1234-56789abcdef0").orElseThrow(),
                                new HashSet<>(campusRepository.getAll()), true)
                );
            } else {
                new Admin(testUser.get().getId(), new User(UUID.randomUUID().toString(), email, passwordEncoder.encode("FocusYourLife2025--"), "Usuario", "", "Test", "", "Usuario test" + i, "0999999999", List.of(new UserEntities("3936ae5e-0cc1-4375-abc7-520d16999110", "ADMIN"))),
                        adminRoleRepository.findById("c1b2a3d4-e5f6-7890-1234-56789abcdef0").orElseThrow(),
                        new HashSet<>(campusRepository.getAll()), true);
            }
        }
    }

    @ShellMethod(key = "app:seed-fake-data")
    private void mock() {
        makeTrainer();
        makePrograms();
        makeStaffs();
        makeVisionaries();
        makeMasterLife();
        makeParticipants();
    }


    private void makeTrainer() {
        for (int i = 0; i < 250; i++) {
            var trainer = Trainer.create(UUID.randomUUID().toString(), faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().phoneNumber(), faker.internet().password(), true);
            trainerCreatorService.createTrainer(trainer);
        }
    }

    private void makePrograms() {
        TrainingAutoGenerateRequest request = new TrainingAutoGenerateRequest();
        request.firstFocus = 100;
        request.startDate = LocalDate.of(2025, 3, 21);
        request.campusId = "61d88b2a-a22e-4cb0-8e43-e036483039d6";
        request.numberOfFocus = 10;
        commandTrainingService.autoGenerateTraining(request);
    }

    private void makeStaffs() {
        for (int i = 0; i < 100; i++) {
            var staffId = UUID.randomUUID().toString();
            var user = new User(UUID.randomUUID().toString(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().firstName(), faker.name().lastName(), faker.name().lastName(), faker.name().fullName(), faker.phoneNumber().phoneNumber(), List.of(new UserEntities(staffId, UserType.STAFF.toString())));
            var trainer = Staff.create(staffId, "STAFF", true, user);
            staffCreatorService.create(trainer);
        }
    }

    private void makeMasterLife() {
        for (int i = 0; i < 100; i++) {
            var staffId = UUID.randomUUID().toString();
            var user = new User(UUID.randomUUID().toString(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().firstName(), faker.name().lastName(), faker.name().lastName(), faker.name().fullName(), faker.phoneNumber().phoneNumber(), List.of(new UserEntities(staffId, UserType.STAFF.toString())));
            var trainer = MasterLife.create(staffId, true, user);
            commandMasterLifeService.save(trainer);
        }
    }

    private void makeVisionaries() {
        for (int i = 0; i < 100; i++) {
            var staffId = UUID.randomUUID().toString();
            var user = new User(UUID.randomUUID().toString(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().firstName(), faker.name().lastName(), faker.name().lastName(), faker.name().fullName(), faker.phoneNumber().phoneNumber(), List.of(new UserEntities(staffId, UserType.STAFF.toString())));
            var trainer = Visionary.create(staffId, true, user);
            visionaryCreatorService.create(trainer);
        }
    }

    private void makeParticipants() {
        try {
            var invitationToken = commandInvitationService.createInvitationByAdminWithQuantity(new InvitationRequest("3936ae5e-0cc1-4375-abc7-520d16999110", "600", "61d88b2a-a22e-4cb0-8e43-e036483039d6"));
            for (int i = 0; i < 350; i++) {
                var staffId = UUID.randomUUID().toString();
                var user = new User(UUID.randomUUID().toString(), faker.internet().emailAddress(), passwordEncoder.encode("focus2025"), faker.name().firstName(), faker.name().firstName(), faker.name().lastName(), faker.name().lastName(), faker.name().fullName(), faker.phoneNumber().phoneNumber(), List.of(new UserEntities(staffId, UserType.STAFF.toString())));
                var bd = faker.date().birthday().toInstant();
                System.out.println(bd + "==================");
                var profile = new ProfileDetailRequest(null, Date.from(bd), faker.address().fullAddress(), faker.job().position(), faker.gender().shortBinaryTypes(), "SOLTERO", faker.idNumber().inValidEnZaSsn(), faker.address().city()).toDomain();
                var trainer = Participant.create(staffId, user, null, profile, invitationToken, false, false, queryInvitationServices.findInvitationByToken(invitationToken).getCampus());
                var medicalRecord = new MedicalRecordSaveRequest("N/A", "N/A", "N/A");
                participantCommandService.createInitUser(trainer, medicalRecord, new SaveContactRequest(null, faker.name().fullName(), "FAMILY", faker.phoneNumber().phoneNumber(), null), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
