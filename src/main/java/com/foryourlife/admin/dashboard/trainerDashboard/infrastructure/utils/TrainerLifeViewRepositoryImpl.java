package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.*;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.invitations.domain.EnrolledUsers;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TrainerLifeViewRepositoryImpl implements TrainerViewRepository {
    private final TrainingRepository jpaTrainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final PromiseRepository promiseRepository;
    private final TeamRepository teamRepository;
    private final InvitationRepository invitationRepository;

    public TrainerLifeViewRepositoryImpl(TrainingRepository jpaTrainingRepository, AttendanceRepository attendanceRepository, TrainingDashboardUtils trainingDashboardUtils, PromiseRepository promiseRepository, TeamRepository teamRepository, InvitationRepository invitationRepository) {
        this.jpaTrainingRepository = jpaTrainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.promiseRepository = promiseRepository;
        this.teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
    }

    @Transactional
    @Override
    public List<TrainerLifeView> getTrainerLifeViewByTraining(String trainingId) {
        var training = jpaTrainingRepository.findById(trainingId)
                .orElseThrow(() -> new BaseException("Entrenamiento no encontrado", List.of()));

        Training life1 = null, life2 = null, life3 = null, life4 = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_GRADUATE)) {
            life4 = training;
            life3 = jpaTrainingRepository.findByNextLevel_Id(life4.getId()).orElse(null);
            life2 = (life3 != null) ? jpaTrainingRepository.findByNextLevel_Id(life3.getId()).orElse(null) : null;
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        }

        List<TrainerLifeView> dashboards = new ArrayList<>();
        if (life1 != null) dashboards.add(buildTrainingDashboard(life1));
        if (life2 != null) dashboards.add(buildTrainingDashboard(life2));
        if (life3 != null) dashboards.add(buildTrainingDashboard(life3));
        if (life4 != null) dashboards.add(buildTrainingDashboard(life4));

        return dashboards;
    }

    private TrainerLifeView buildTrainingDashboard(Training training) {
        if (training == null) return null;

        List<Attendance> attendances = attendanceRepository.findAttendanceByTraining(training.getId());
        List<Promise> promises = promiseRepository.findByTrainingId(training.getId());
        Map<String, Participant> participants = trainingDashboardUtils.loadParticipants(attendances);

        Map<String, Promise> promiseMap = promises.stream()
                .filter(p -> p.getUser() != null)
                .collect(Collectors.toMap(p -> p.getUser().getId(), p -> p, (a, b) -> a));

        List<UserDashboardDto> userDashboards = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {
                    User user = a.getUser();
                    String entity = trainingDashboardUtils.resolveUserType(user);
                    Promise userPromise = promiseMap.get(a.getUser().getId());
                    return new UserDashboardDto(
                            user.getName(),
                            entity,
                            a.getFridayAttendance(),
                            a.getSaturdayAttendance(),
                            a.getSundayAttendance(),
                            userPromise != null ? userPromise.getFirstPromise() : 0,
                            userPromise != null ? userPromise.getSecondPromise() : 0,
                            userPromise != null ? userPromise.getThirdPromise() : 0,
                            userPromise != null ? userPromise.getAchievedCount() : 0,
                            userPromise != null ? userPromise.getPaidCount() : 0
                    );
                }).toList();

        var team = Optional.ofNullable(training.getOriginalTeam())
                .or(() -> teamRepository.findByTrainingId(training.getId()))
                .orElseThrow(() -> new BaseException(
                        "No existe equipo para el entrenamiento con id: " + training.getId(),
                        List.of("TEAM_NOT_FOUND")
                ));

        LifeAttendanceDashboard lifeAttendanceDashboard = buildAttendanceDashboard(attendances, participants, team.getMasterLife(), userDashboards);

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        var previousTrainingStats = trainingDashboardUtils.buildPreviousTrainingStats(
                attendances.getFirst().getTraining(),
                attendances,
                participants
        );

        return new TrainerLifeView(
                training.getName(),
                trainerName,
                training.getStartDate().toString() + " - " + training.getEndDate().toString(),
                training.getCourseLevelDisplay(),
                previousTrainingStats,
                userDashboards,
                buildDeclarationStats(userDashboards),
                lifeAttendanceDashboard,
                trainingDashboardUtils.buildLingererStats(training, attendances, participants)
        );
    }

    public LifeAttendanceDashboard buildAttendanceDashboard(List<Attendance> attendances, Map<String, Participant> participants, List<MasterLife> masterLifes, List<UserDashboardDto> users) {
        if (attendances == null || attendances.isEmpty()) {
            return new LifeAttendanceDashboard(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0);
        }

        Predicate<Attendance> isParticipant = a ->
                a.getUser() != null &&
                        a.getUser().getEntityMap() != null &&
                        a.getUser().getEntityMap().stream().anyMatch(e ->
                                e.getEntity().equals(UserType.PARTICIPANT.name())
                        ) &&
                        a.getUser().getEntityMap().stream().noneMatch(e ->
                                e.getEntity().equals(UserType.MASTER_LIFE.name())
                        );

        Predicate<Attendance> isMasterLife = a ->
                a.getUser() != null &&
                        a.getUser().getEntityMap() != null &&
                        a.getUser().getEntityMap().stream()
                                .anyMatch(e -> e.getEntity().equals(UserType.MASTER_LIFE.name()));

        int totalParticipants = (int) attendances.stream().filter(isParticipant).count();

        int totalMasterLifes = (int) attendances.stream().filter(isMasterLife).count();

        int totalUsers = totalParticipants + totalMasterLifes;

        int sundayCount = (int) attendances.stream().filter(isParticipant).filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO).count();

        int masterSundayCount = (int) attendances.stream().filter(isMasterLife).filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO).count();

        int deserterCount = (int) attendances.stream()
                .filter(a -> a.getFridayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getFridayAttendance() == AttendanceStatus.DESERTO ||
                        a.getSaturdayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getSaturdayAttendance() == AttendanceStatus.DESERTO ||
                        a.getSundayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getSundayAttendance() == AttendanceStatus.DESERTO
                )
                .count();

        int totalAttendancesCount = sundayCount + masterSundayCount;

        double deserterPercentage = totalAttendancesCount > 0
                ?(double) deserterCount / totalAttendancesCount
                : 0.0;

        var training = attendances.getFirst().getTraining();

        List<String> invitationTokens = participants.values().stream()
                .map(Participant::getInvitationToken)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, Invitation> invitationsByToken = invitationTokens.isEmpty()
                ? Map.of()
                : invitationRepository.findAllByTokenIn(invitationTokens)
                  .stream()
                  .collect(Collectors.toMap(
                          Invitation::getToken,
                          i -> i
                  ));

        List<Invitation> trainingInvitations = invitationsByToken.values().stream()
                .filter(invitation ->
                        invitation.getEnrolled() != null &&
                                Objects.equals(
                                        invitation.getEnrolled().getTrainingName(),
                                        training.getName()
                                )
                )
                .toList();

        List<String> masterLifesIds = masterLifes.stream().map(MasterLife::getId).toList();

        Map<String, Invitation> masterLifesInvitations = masterLifesIds.isEmpty()
                ? Map.of()
                : invitationRepository.findAllByTokenIn(masterLifesIds)
                  .stream()
                  .collect(Collectors.toMap(
                          Invitation::getSenderId,
                          i -> i
                  ));

        List<Invitation> masterInvitations = masterLifesInvitations.values().stream()
                .filter(invitation ->
                        invitation.getEnrolled() != null &&
                                Objects.equals(
                                        invitation.getEnrolled().getTrainingName(),
                                        training.getName()
                                )
                )
                .toList();

        int participantEnrolledCount = users.stream()
                .filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() != null
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int masterLifesEnrolledCount = users.stream()
                .filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() != null
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int totalEnrolledCount = participantEnrolledCount + masterLifesEnrolledCount;

        int participantInvitationUsed = users.stream()
                .filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() > 0
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int masterInvitationUsed = users.stream()
                .filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() > 0
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int totalInvitationUsed = participantInvitationUsed + masterInvitationUsed;

        double enrollmentIndex = totalAttendancesCount > 0
                ? Math.round((double) totalEnrolledCount / totalAttendancesCount)
                : 0.0;

        double realEnrollmentIndex = totalUsers > 0
                ? Math.round((double) totalEnrolledCount / totalUsers)
                : 0.0;


        double totalUsersEnrollersPercentage = totalAttendancesCount > 0
                ? Math.round(((double) totalInvitationUsed / totalAttendancesCount) * 100.0) / 100.0
                : 0.0;

        List<String> userIds = trainingInvitations.stream()
                .filter(invitation -> invitation.getUsers() != null)
                .flatMap(invitation -> invitation.getUsers().stream())
                .map(EnrolledUsers::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<String> mastersIds = masterInvitations.stream()
                .filter(invitation -> invitation.getUsers() != null)
                .flatMap(invitation -> invitation.getUsers().stream())
                .map(EnrolledUsers::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();


        var focusAttendances = attendanceRepository.findAllByTrainingNumberAndUsersIdsAndCampusId(
                training.getNumber(),
                userIds,
                training.getCampus().getId()
        );

        var masterLifeFocusAttendances = attendanceRepository.findAllByTrainingNumberAndUsersIdsAndCampusId(
                training.getNumber(),
                mastersIds,
                training.getCampus().getId()
        );

        int focusAttendancesCount = (int) focusAttendances.stream()
                .filter(a ->
                        a.getFridayAttendance() == AttendanceStatus.ASISTIO ||
                                a.getSaturdayAttendance() == AttendanceStatus.ASISTIO ||
                                a.getSundayAttendance() == AttendanceStatus.ASISTIO
                )
                .count();

        int focusAttendancesCountMasterLife = (int) masterLifeFocusAttendances.stream()
                .filter(a ->
                        a.getFridayAttendance() == AttendanceStatus.ASISTIO ||
                                a.getSaturdayAttendance() == AttendanceStatus.ASISTIO ||
                                a.getSundayAttendance() == AttendanceStatus.ASISTIO
                )
                .count();

        int totalFocusAttendances = focusAttendancesCount + focusAttendancesCountMasterLife;

        double enrollmentEffectiveness = userIds.isEmpty()
                ? 0.0
                : Math.round(((double) totalFocusAttendances / userIds.size()) * 100.0) / 100.0;

        int focusDeclarationsCount = 0;
        double focusDeclarationPercentage = 0.0;
        int staffMembersCount = 0;
        double staffMembersPercentage = 0.0;

        if (training.getCourseLevel().equals(CourseLevel.LIFE_GRADUATE)){

        }

        return new LifeAttendanceDashboard(
                totalParticipants,
                totalMasterLifes,
                totalUsers,
                sundayCount,
                masterSundayCount,
                totalAttendancesCount,
                deserterCount,
                deserterPercentage,
                participantEnrolledCount,
                masterLifesEnrolledCount,
                totalEnrolledCount,
                enrollmentIndex,
                realEnrollmentIndex,
                totalInvitationUsed,
                totalUsersEnrollersPercentage,
                totalFocusAttendances,
                enrollmentEffectiveness,
                focusDeclarationsCount,
                focusDeclarationPercentage,
                staffMembersCount,
                staffMembersPercentage
        );
    }

    public DeclarationStats buildDeclarationStats(List<UserDashboardDto> userDashboardDtos) {
        if (userDashboardDtos == null || userDashboardDtos.isEmpty()) {
            return new DeclarationStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0, 0, 0, 0.0, 0.0, 0.0, 0, 0, 0.0);
        }

        int masterLifePromisesCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getThirdPromise() > 0
                                ? userDashboardDto.getThirdPromise()
                                : 0
                )
                .sum();

        int participantPromisesCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getThirdPromise() > 0
                                ? userDashboardDto.getThirdPromise()
                                : 0
                )
                .sum();

        int totalTeamPromisesCount = masterLifePromisesCount + participantPromisesCount;

        int masterLifeAchievedCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getAchievedCount() > 0
                                ? userDashboardDto.getAchievedCount()
                                : 0
                )
                .sum();

        int participantAchievedCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getAchievedCount() > 0
                                ? userDashboardDto.getAchievedCount()
                                : 0
                )
                .sum();

        int totalTeamAchievedCount = masterLifeAchievedCount + participantAchievedCount;

        int masterLifePaidCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() > 0
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int participantPaidCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto ->
                        userDashboardDto.getPaidCount() > 0
                                ? userDashboardDto.getPaidCount()
                                : 0
                )
                .sum();

        int totalTeamPaidCount = masterLifePaidCount + participantPaidCount;

        double accomplishmentMasterLife = masterLifePaidCount > 0
                ? (double) masterLifePromisesCount / masterLifePaidCount
                : 0.0;

        double accomplishmentParticipant = participantPaidCount > 0
                ? (double) participantPromisesCount / participantPaidCount
                : 0.0;

        double accomplishmentTeam = totalTeamPaidCount > 0
                ? (double) totalTeamPromisesCount / totalTeamPaidCount
                : 0.0;

        int masterLifeCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.MASTER_LIFE.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto -> 1)
                .sum();

        int participantsCount = userDashboardDtos.stream().filter(userDashboardDto -> userDashboardDto.getUserEntity() != null)
                .filter(userDashboardDto -> UserType.PARTICIPANT.getValue().equals(userDashboardDto.getUserEntity()))
                .mapToInt(userDashboardDto -> 1)
                .sum();

        int teamCount = masterLifeCount + participantsCount;

        double masterLifeEnrollmentIndex = masterLifePaidCount > 0
                ? (double) masterLifeCount / masterLifePaidCount
                : 0.0;

        double participantEnrollmentIndex = participantPaidCount > 0
                ? (double) participantsCount / participantPaidCount
                : 0.0;

        double teamEnrollmentIndex = totalTeamPaidCount > 0
                ? (double) teamCount / totalTeamPaidCount
                : 0.0;

        int totalUsersNotEnrolledCount = (int) userDashboardDtos.stream()
                .filter(user -> user.getUserEntity() != null)
                .filter(user -> Optional.ofNullable(user.getPaidCount()).orElse(0) <= 0)
                .count();

        int totalUsersEnrollersCount = (int) userDashboardDtos.stream()
                .filter(user -> user.getUserEntity() != null)
                .filter(user -> Optional.ofNullable(user.getPaidCount()).orElse(0) > 0)
                .count();

        double totalUsersEnrollersPercentage = teamCount > 0
                ? (double) totalUsersEnrollersCount / teamCount
                : 0.0;

        return new DeclarationStats(
                masterLifePromisesCount,
                masterLifeAchievedCount,
                masterLifePaidCount,
                participantPromisesCount,
                participantAchievedCount,
                participantPaidCount,
                totalTeamPromisesCount,
                totalTeamAchievedCount,
                totalTeamPaidCount,
                accomplishmentMasterLife,
                accomplishmentParticipant,
                accomplishmentTeam,
                masterLifeCount,
                participantsCount,
                teamCount,
                masterLifeEnrollmentIndex,
                participantEnrollmentIndex,
                teamEnrollmentIndex,
                totalUsersNotEnrolledCount,
                totalUsersEnrollersCount,
                totalUsersEnrollersPercentage
        );
    }

    @Override
    public String generateExcelReport(List<TrainerLifeView> trainerLifeViews) {
        // If no data, return empty
        if (trainerLifeViews == null || trainerLifeViews.isEmpty()) {
            return "";
        }

        // Use Apache POI to build an XLSX workbook with one sheet per TrainerLifeView
        org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

        int sheetIndex = 0;
        for (TrainerLifeView view : trainerLifeViews) {
            String baseName = Optional.ofNullable(view.getTrainingName()).orElse("training_") + (sheetIndex > 0 ? "_" + sheetIndex : "");
            // Excel sheet name limit is 31 chars and cannot contain some characters
            String sheetName = baseName;
            // replace forbidden characters without using regex to avoid escaping issues
            sheetName = sheetName.replace('\\', '_').replace('/', '_').replace(':', '_').replace('?', '_').replace('*', '_').replace('[', '_').replace(']', '_');
            if (sheetName.length() > 31) sheetName = sheetName.substring(0, 31);

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet(sheetName);

            int rownum = 0;

            // Header info rows
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(rownum++);
            headerRow.createCell(0).setCellValue("Training");
            headerRow.createCell(1).setCellValue(Optional.ofNullable(view.getTrainingName()).orElse(""));

            org.apache.poi.ss.usermodel.Row trainerRow = sheet.createRow(rownum++);
            trainerRow.createCell(0).setCellValue("Trainer");
            trainerRow.createCell(1).setCellValue(Optional.ofNullable(view.getTrainerName()).orElse(""));

            org.apache.poi.ss.usermodel.Row dateRow = sheet.createRow(rownum++);
            dateRow.createCell(0).setCellValue("Dates");
            dateRow.createCell(1).setCellValue(Optional.ofNullable(view.getTrainingDate()).orElse(""));

            org.apache.poi.ss.usermodel.Row levelRow = sheet.createRow(rownum++);
            levelRow.createCell(0).setCellValue("Level");
            levelRow.createCell(1).setCellValue(Optional.ofNullable(view.getCourseLevel()).orElse(""));

            rownum++; // empty row

            // Declaration stats (if present)
            var decl = view.getDeclarationStats();
            if (decl != null) {
                org.apache.poi.ss.usermodel.Row declTitle = sheet.createRow(rownum++);
                declTitle.createCell(0).setCellValue("Declaration Stats");

                org.apache.poi.ss.usermodel.Row declHeader = sheet.createRow(rownum++);
                declHeader.createCell(0).setCellValue("MasterPromises");
                declHeader.createCell(1).setCellValue("ParticipantPromises");
                declHeader.createCell(2).setCellValue("TotalPromises");
                declHeader.createCell(3).setCellValue("MasterAchieved");
                declHeader.createCell(4).setCellValue("ParticipantAchieved");
                declHeader.createCell(5).setCellValue("TotalAchieved");

                org.apache.poi.ss.usermodel.Row declValues = sheet.createRow(rownum++);
                declValues.createCell(0).setCellValue(decl.getTotalMasterLifePromisesCount());
                declValues.createCell(1).setCellValue(decl.getTotalParticipantPromisesCount());
                declValues.createCell(2).setCellValue(decl.getTotalTeamLifePromisesCount());
                declValues.createCell(3).setCellValue(decl.getTotalMasterLifeAchievedCount());
                declValues.createCell(4).setCellValue(decl.getTotalParticipantAchievedCount());
                declValues.createCell(5).setCellValue(decl.getTotalTeamAchievedCount());

                rownum++; // empty row
            }

            // Attendance dashboard
            var att = view.getLifeAttendanceDashboard();
            if (att != null) {
                org.apache.poi.ss.usermodel.Row attTitle = sheet.createRow(rownum++);
                attTitle.createCell(0).setCellValue("Attendance Stats");

                org.apache.poi.ss.usermodel.Row attHeader = sheet.createRow(rownum++);
                attHeader.createCell(0).setCellValue("Participants");
                attHeader.createCell(1).setCellValue("MasterParticipants");
                attHeader.createCell(2).setCellValue("TotalUsers");
                attHeader.createCell(3).setCellValue("ParticipantAttendancesCount");
                attHeader.createCell(4).setCellValue("TotalAttendances");
                attHeader.createCell(5).setCellValue("DeserterCount");

                org.apache.poi.ss.usermodel.Row attValues = sheet.createRow(rownum++);
                attValues.createCell(0).setCellValue(att.getTotalParticipants());
                attValues.createCell(1).setCellValue(att.getTotalMasterParticipants());
                attValues.createCell(2).setCellValue(att.getTotalTotalUsers());
                attValues.createCell(3).setCellValue(att.getParticipantAttendancesCount());
                attValues.createCell(4).setCellValue(att.getTotalAttendancesCount());
                attValues.createCell(5).setCellValue(att.getDeserterParticipantsCount());

                rownum++; // empty row
            }

            // Users table
            var users = view.getUsers();
            if (users != null && !users.isEmpty()) {
                org.apache.poi.ss.usermodel.Row usersTitle = sheet.createRow(rownum++);
                usersTitle.createCell(0).setCellValue("Users");

                org.apache.poi.ss.usermodel.Row usersHeader = sheet.createRow(rownum++);
                String[] userCols = new String[]{"Name", "Entity", "Friday", "Saturday", "Sunday", "FirstPromise", "SecondPromise", "ThirdPromise", "Achieved", "Paid"};
                for (int i = 0; i < userCols.length; i++) usersHeader.createCell(i).setCellValue(userCols[i]);

                for (UserDashboardDto u : users) {
                    org.apache.poi.ss.usermodel.Row r = sheet.createRow(rownum++);
                    r.createCell(0).setCellValue(Optional.ofNullable(u.getUserName()).orElse(""));
                    r.createCell(1).setCellValue(Optional.ofNullable(u.getUserEntity()).orElse(""));
                    r.createCell(2).setCellValue(Optional.ofNullable(u.getFridayAttendance()).map(Object::toString).orElse(""));
                    r.createCell(3).setCellValue(Optional.ofNullable(u.getSaturdayAttendance()).map(Object::toString).orElse(""));
                    r.createCell(4).setCellValue(Optional.ofNullable(u.getSundayAttendance()).map(Object::toString).orElse(""));
                    r.createCell(5).setCellValue(Optional.ofNullable(u.getFirstPromise()).orElse(0));
                    r.createCell(6).setCellValue(Optional.ofNullable(u.getSecondPromise()).orElse(0));
                    r.createCell(7).setCellValue(Optional.ofNullable(u.getThirdPromise()).orElse(0));
                    r.createCell(8).setCellValue(Optional.ofNullable(u.getAchievedCount()).orElse(0));
                    r.createCell(9).setCellValue(Optional.ofNullable(u.getPaidCount()).orElse(0));
                }
            }

            // auto-size first several columns for readability
            for (int c = 0; c < 10; c++) {
                try {
                    sheet.autoSizeColumn(c);
                } catch (Exception ignored) {
                }
            }

            sheetIndex++;
        }

        // Write workbook to a temp file and return path
        String tmpDir = System.getProperty("java.io.tmpdir");
        java.io.File outFile = new java.io.File(tmpDir, "trainer_life_report_" + System.currentTimeMillis() + ".xlsx");
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outFile)) {
            workbook.write(fos);
            workbook.close();
            return outFile.getAbsolutePath();
        } catch (java.io.IOException e) {
            // In case of error return empty string (alternatively could throw a runtime exception)
            try { workbook.close(); } catch (Exception ex) { }
            return "";
        }
    }
}
