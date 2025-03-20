package com.foryourlife.mock;

import com.foryourlife.admin.programs.trainer.application.TrainerCreatorService;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import com.foryourlife.admin.programs.training.infrastructure.httpControllers.TrainingAutoGenerateRequest;
import com.foryourlife.clients.account.invitations.applications.CommandInvitationService;
import com.foryourlife.clients.account.invitations.infrastructure.InvitationRequest;
import com.foryourlife.clients.account.medicalRecord.domain.MedicalRecord;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.clients.account.user.infrastructure.httpControllers.MedicalRecordSaveRequest;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.staff.application.StaffCreatorService;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.application.VisionaryCreatorService;
import com.foryourlife.visionary.domain.Visionary;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
public class MockData {
    private final TrainerCreatorService trainerCreatorService;
    private final CommandTrainingService commandTrainingService;
    private final StaffCreatorService staffCreatorService;
    private final VisionaryCreatorService visionaryCreatorService;
    private final CommandUsersService commandUsersService;
    private final CommandInvitationService commandInvitationService;
    private final Faker faker = new Faker();

    public MockData(TrainerCreatorService trainerCreatorService, CommandTrainingService commandTrainingService, StaffCreatorService staffCreatorService, VisionaryCreatorService visionaryCreatorService, CommandUsersService commandUsersService, CommandInvitationService commandInvitationService) {
        this.trainerCreatorService = trainerCreatorService;
        this.commandTrainingService = commandTrainingService;
        this.staffCreatorService = staffCreatorService;
        this.visionaryCreatorService = visionaryCreatorService;
        this.commandUsersService = commandUsersService;
        this.commandInvitationService = commandInvitationService;
    }

    @Bean
    CommandLineRunner initMock() {
        return args -> {
            makeTrainer();
            makePrograms();
            makeStaffs();
            makeVisionaries();
            makeParticipants();
        };
    }

    private void makeTrainer() {
        for (int i = 0; i < 50; i++) {
            var trainer = Trainer.create(
                    UUID.randomUUID().toString(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().password(),
                    true
            );
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
            var user = new User(
                    UUID.randomUUID().toString(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().password(),
                    List.of(new UserEntities(staffId, UserType.STAFF.toString()))
            );
            var trainer = Staff.create(
                    staffId,
                    faker.name().fullName(),
                    true,
                    user
            );
            staffCreatorService.create(trainer);
        }
    }

    private void makeVisionaries() {
        for (int i = 0; i < 100; i++) {
            var staffId = UUID.randomUUID().toString();
            var user = new User(
                    UUID.randomUUID().toString(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().password(),
                    List.of(new UserEntities(staffId, UserType.STAFF.toString()))
            );
            var trainer = Visionary.create(
                    staffId,
                    faker.name().fullName(),
                    true,
                    user
            );
            visionaryCreatorService.create(trainer);
        }
    }

    private void makeParticipants() {
        var invitationToken = commandInvitationService.createInvitationByAdminWithQuantity(new InvitationRequest("3936ae5e-0cc1-4375-abc7-520d16999110", "600"));
        for (int i = 0; i < 500; i++) {
            var staffId = UUID.randomUUID().toString();
            var user = new User(
                    UUID.randomUUID().toString(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().password(),
                    List.of(new UserEntities(staffId, UserType.STAFF.toString()))
            );
            var trainer = Participant.create(
                    staffId,
                    user,
                    null,
                    null,
                    invitationToken
            );
            var medicalRecord = new MedicalRecordSaveRequest(
                    "N/A",
                    "N/A",
                    "N/A"
            );
            commandUsersService.createInitUser(trainer, medicalRecord);
        }
    }
}
