package com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure.persistence;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.*;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserType;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrainerFocusViewRepositoryImpl implements TrainerFocusViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl;
    private final TrainingRepository trainingRepository;
    private final ParticipantRepository participantRepository;
    private final OrganizationChartRepository organizationChartRepository;
    private final InvitationRepository invitationRepository;
    private final PaymentRepository paymentRepository;

    public TrainerFocusViewRepositoryImpl(AttendanceRepository attendanceRepository, TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl, TrainingRepository trainingRepository, ParticipantRepository participantRepository, OrganizationChartRepository organizationChartRepository, InvitationRepository invitationRepository, PaymentRepository paymentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.trainerLifeViewRepositoryImpl = trainerLifeViewRepositoryImpl;
        this.trainingRepository = trainingRepository;
        this.participantRepository = participantRepository;
        this.organizationChartRepository = organizationChartRepository;
        this.invitationRepository = invitationRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public TrainerFocusView getTrainerFocusViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        Map<String, Participant> participants = loadParticipants(attendances);

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

        participants.values().forEach(participant -> {
            String token = participant.getInvitationToken();
            if (token != null) {
                Invitation invitation = invitationsByToken.get(token);
                if (invitation != null) {
                    participant.getUser().setInvitations(List.of(invitation));
                }
            }
        });

        List<String> trainingNames = new ArrayList<>();

        invitationsByToken.values().forEach(invitation -> {
            if (invitation.getEnrolled() != null && invitation.getEnrolled().getTrainingName() != null) {
                String name = invitation.getEnrolled().getTrainingName();
                if (!trainingNames.contains(name)) {
                    trainingNames.add(name);
                }
            }
        });

        Map<String, LifeWeekendAssistant> lifeWeekendAssistants = new HashMap<>();

        for (String trainingName : trainingNames) {

            int enrolled = 0;
            int assistant = 0;

            for (Participant participant : participants.values()) {

                String token = participant.getInvitationToken();
                if (token == null) continue;

                Invitation invitation = invitationsByToken.get(token);
                if (invitation == null || invitation.getEnrolled() == null) continue;

                if (trainingName.equals(invitation.getEnrolled().getTrainingName())) {

                    enrolled++;

                    boolean attended = attendances.stream()
                            .anyMatch(a -> a.getUser().getId().equals(participant.getUser().getId()));

                    if (attended) {
                        assistant++;
                    }
                }
            }

            int percentage = enrolled == 0 ? 0 : (assistant * 100) / enrolled;

            lifeWeekendAssistants.put(
                    trainingName,
                    new LifeWeekendAssistant(assistant, enrolled, percentage)
            );
        }

        var lingererStats = this.buildLingererStats(
                attendances.getFirst().getTraining(),
                attendances,
                participants
        );

        return new TrainerFocusView(
                this.buildGeneralAttendance(attendances, participants),
                this.buildGenderDashboard(attendances, participants),
                this.buildAgeDashboard(attendances, participants),
                this.buildPaymentDashboard(attendances, participants),
                trainingNames,
                lifeWeekendAssistants,
                lingererStats
        );
    }

    public GeneralAttendance buildGeneralAttendance(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {
        List<UserAttendance> userAttendances = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {

                    Participant participant = participants.get(a.getUser().getId());

                    String entity = trainerLifeViewRepositoryImpl.resolveUserType(a.getUser());
                    UserType userType = UserType.fromValue(entity);

                    String invitationInfo = null;

                    if (participant != null && !participant.getUser().getInvitations().isEmpty()) {
                        invitationInfo = participant.getUser().getInvitations().getFirst().getEnrolled().getTrainingName();

                    }

                    return new UserAttendance(
                            a.getUser().getName(),
                            userType,
                            invitationInfo,
                            a.getFridayAttendance(),
                            a.getSaturdayAttendance(),
                            a.getSundayAttendance()
                    );
                })
                .toList();

        int totalLingerers = (int) attendances.stream()
                .filter(a -> {
                    Participant p = participants.get(a.getUser().getId());
                    return p != null && Boolean.TRUE.equals(p.getIsLingerer());
                })
                .count();

        int totalFocus = (int) attendances.stream()
                .filter(a -> {
                    Participant p = participants.get(a.getUser().getId());
                    return p != null && !Boolean.TRUE.equals(p.getIsLingerer());
                })
                .count();

        int totalDistorter = (int) attendances.stream()
                .filter(a -> {
                    Participant p = participants.get(a.getUser().getId());
                    return p != null && !Boolean.TRUE.equals(p.getDesertor());
                })
                .count();

        int initialPx = attendances.getFirst().getTraining().getOriginalTeam().getUsers().size();

        double distortionPercentage = initialPx == 0 ? 0 : ((double) totalDistorter / initialPx) * 100;
        return new GeneralAttendance(initialPx ,totalFocus, totalLingerers,totalDistorter, distortionPercentage,userAttendances);
    }

    public List<GenderDashboard> buildGenderDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        Map<String, String> genderMap = participants.values().stream()
                .collect(Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> p.getProfile().getGender().toUpperCase()
                ));

        BiFunction<Function<Attendance, AttendanceStatus>, String, Long> count =
                (dayExtractor, gender) -> attendances.stream()
                        .filter(a -> gender.equals(genderMap.get(a.getUser().getId())))
                        .filter(a -> dayExtractor.apply(a) == AttendanceStatus.ASISTIO)
                        .count();

        return List.of(
                new GenderDashboard("Viernes",
                        count.apply(Attendance::getFridayAttendance, "H").intValue(),
                        count.apply(Attendance::getFridayAttendance, "M").intValue()
                ),
                new GenderDashboard("Sábado",
                        count.apply(Attendance::getSaturdayAttendance, "H").intValue(),
                        count.apply(Attendance::getSaturdayAttendance, "M").intValue()
                ),
                new GenderDashboard("Domingo",
                        count.apply(Attendance::getSundayAttendance, "H").intValue(),
                        count.apply(Attendance::getSundayAttendance, "M").intValue()
                )
        );
    }

    public List<AgeDashboard> buildAgeDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        Function<Integer, String> classifyAge = age -> {
            if (age == null) return "unknown";

            if (age < 18) return "less_18";
            if (age <= 27) return "18_27";
            if (age <= 40) return "28_40";
            if (age <= 65) return "41_65";
            return "above_65";
        };

        Map<String, String> ageMap = participants.values().stream()
                .collect(Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> {
                            Integer age = calculateAge(LocalDate.ofInstant(p.getProfile().getBirthday().toInstant(), ZoneId.systemDefault()));
                            return classifyAge.apply(age);
                        }
                ));

        BiFunction<Function<Attendance, AttendanceStatus>, String, Long> countByAgeRange =
                (dayExtractor, ageRange) -> attendances.stream()
                        .filter(a -> dayExtractor.apply(a) == AttendanceStatus.ASISTIO)
                        .filter(a -> ageRange.equals(ageMap.get(a.getUser().getId())))
                        .count();

        AgeDashboard friday = new AgeDashboard();
        friday.day = "Viernes";
        friday.age_less_18 = countByAgeRange.apply(Attendance::getFridayAttendance, "less_18").intValue();
        friday.age_18_27 = countByAgeRange.apply(Attendance::getFridayAttendance, "18_27").intValue();
        friday.age_28_40 = countByAgeRange.apply(Attendance::getFridayAttendance, "28_40").intValue();
        friday.age_41_65 = countByAgeRange.apply(Attendance::getFridayAttendance, "41_65").intValue();
        friday.age_above_65 = countByAgeRange.apply(Attendance::getFridayAttendance, "above_65").intValue();

        AgeDashboard saturday = new AgeDashboard();
        saturday.day = "Sábado";
        saturday.age_less_18 = countByAgeRange.apply(Attendance::getSaturdayAttendance, "less_18").intValue();
        saturday.age_18_27 = countByAgeRange.apply(Attendance::getSaturdayAttendance, "18_27").intValue();
        saturday.age_28_40 = countByAgeRange.apply(Attendance::getSaturdayAttendance, "28_40").intValue();
        saturday.age_41_65 = countByAgeRange.apply(Attendance::getSaturdayAttendance, "41_65").intValue();
        saturday.age_above_65 = countByAgeRange.apply(Attendance::getSaturdayAttendance, "above_65").intValue();

        AgeDashboard sunday = new AgeDashboard();
        sunday.day = "Domingo";
        sunday.age_less_18 = countByAgeRange.apply(Attendance::getSundayAttendance, "less_18").intValue();
        sunday.age_18_27 = countByAgeRange.apply(Attendance::getSundayAttendance, "18_27").intValue();
        sunday.age_28_40 = countByAgeRange.apply(Attendance::getSundayAttendance, "28_40").intValue();
        sunday.age_41_65 = countByAgeRange.apply(Attendance::getSundayAttendance, "41_65").intValue();
        sunday.age_above_65 = countByAgeRange.apply(Attendance::getSundayAttendance, "above_65").intValue();

        return List.of(friday, saturday, sunday);
    }


    public Map<String, Participant> loadParticipants(List<Attendance> attendances) {
        List<String> userIds = attendances.stream()
                .map(a -> a.getUser().getId())
                .distinct()
                .toList();

        return participantRepository.findAllByUserIds(userIds)
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> p
                ));
    }

    private Integer calculateAge(LocalDate birthday) {
        if (birthday == null) return null;
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public List<PaymentDashboard> buildPaymentDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        if (attendances.isEmpty()) return List.of();

        var training = attendances.getFirst().getTraining();
        LocalDate sunday = training.getEndDate();

        var organizationChart = organizationChartRepository
                .getOrganizationChartTrainingId(training.getId());

        if (organizationChart.isEmpty()) return List.of();

        List<ChartNode> allNodes = flattenTree(organizationChart.get().getNodes());

        List<ChartNode> staffNodes = allNodes.stream()
                .filter(n -> n.getLevel() == UserType.STAFF)
                .toList();

        List<ChartNode> participantNodes = allNodes.stream()
                .filter(n -> n.getLevel() == UserType.PARTICIPANT)
                .toList();


        // staff -> participants
        Map<String, List<String>> staffToParticipants = participantNodes.stream()
                .collect(Collectors.groupingBy(
                        ChartNode::getParentNodeId,
                        Collectors.mapping(n -> n.getMembers().getId(), Collectors.toList())
                ));


        // obtener todos los pagos
        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        // pagos por participante
        Map<String, List<Payment>> paymentsByParticipantId = payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getParticipant().getUser().getId()
                ));


        List<PaymentDashboard> dashboards = new ArrayList<>();


        for (ChartNode staffNode : staffNodes) {

            List<String> staffParticipantIds =
                    staffToParticipants.getOrDefault(staffNode.getId(), List.of());

            // todos los pagos de los participantes de ese staff
            List<Payment> allPayments = staffParticipantIds.stream()
                    .flatMap(pId -> paymentsByParticipantId
                            .getOrDefault(pId, List.of())
                            .stream())
                    .toList();


            // pagos hasta domingo
            List<Payment> sundayPayments = allPayments.stream()
                    .filter(p -> !p.getCreatedDate().toLocalDate().isAfter(sunday))
                    .toList();


            // ---------- DOMINGO ----------

            int yourSunday = (int) sundayPayments.stream()
                    .filter(p -> p.getTraining() != null
                            && CourseLevel.YOUR.equals(p.getTraining().getCourseLevel())
                            && p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int lifeSunday = (int) sundayPayments.stream()
                    .filter(p -> p.getTraining() != null
                            && CourseLevel.YOUR.equals(p.getTraining().getCourseLevel())
                            && p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int yourPlusLifeSunday = yourSunday + lifeSunday;

            int totalSunday = sundayPayments.size();




            int yourFinal = (int) allPayments.stream()
                    .filter(p -> p.getTraining() != null
                            && CourseLevel.YOUR.equals(p.getTraining().getCourseLevel())
                            && p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int lifeFinal = (int) allPayments.stream()
                    .filter(p -> p.getTraining() != null
                            && CourseLevel.YOUR.equals(p.getTraining().getCourseLevel())
                            && p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int yourPlusLifeFinal = yourFinal + lifeFinal;

            int totalFinal = allPayments.size();

            int totalFocus = (int) attendances.stream()
                    .filter(a -> {
                        Participant p = participants.get(a.getUser().getId());
                        return p != null && !Boolean.TRUE.equals(p.getIsLingerer());
                    })
                    .count();

            double passPercentageSunday = totalSunday == 0
                    ? 0
                    : ((double) yourPlusLifeSunday / totalFocus) * 100;


            double passPercentageTotal = totalFinal == 0
                    ? 0
                    : ((double) yourPlusLifeFinal / totalFocus) * 100;
            dashboards.add(
                   new PaymentDashboard(
                            staffNode.getMembers().getName(),
                            yourSunday,
                            yourPlusLifeSunday,
                            totalSunday,
                            passPercentageSunday,
                            yourFinal,
                            yourPlusLifeFinal,
                            totalFinal,
                           passPercentageTotal
                    )
            );
        }

        return dashboards;
    }

    private List<ChartNode> flattenTree(List<ChartNode> nodes) {
        List<ChartNode> all = new ArrayList<>();
        for (ChartNode n : nodes) {
            all.add(n);
            if (n.getChildren() != null && !n.getChildren().isEmpty()) {
                all.addAll(flattenTree(n.getChildren()));
            }
        }
        return all;
    }

    public LingererStats buildLingererStats(
            Training currentTraining,
            List<Attendance> currentAttendances,
            Map<String, Participant> participants
    ) {

        int total = 0;
        int attended = 0;
        int notAttended = 0;

        int previous1 = 0;
        int previous2 = 0;
        int others = 0;

        for (Attendance attendance : currentAttendances) {

            Participant participant = participants.get(attendance.getUser().getId());

            if (participant == null) continue;

            if (!Boolean.TRUE.equals(participant.getIsLingerer())) continue;

            total++;

            boolean hasAttendance =
                    attendance.getFridayAttendance() == AttendanceStatus.ASISTIO
                            || attendance.getSaturdayAttendance() == AttendanceStatus.ASISTIO
                            || attendance.getSundayAttendance() == AttendanceStatus.ASISTIO;

            if (hasAttendance) attended++;
            else notAttended++;

            List<Attendance> pastAttendances =
                    attendanceRepository.findAttendanceByUser(attendance.getUser().getId())
                            .stream()
                            .filter(a ->
                                    !a.getTraining().getId().equals(currentTraining.getId()) &&
                                            a.getTraining().getEndDate().isBefore(currentTraining.getEndDate())
                            )
                            .sorted(Comparator.comparing(
                                    (Attendance a) -> a.getTraining().getEndDate()
                            ).reversed())
                            .toList();

            for (int i = 0; i < pastAttendances.size(); i++) {

                if (i == 0) previous1++;
                else if (i == 1) previous2++;
                else others++;

                break;
            }
        }

        return new LingererStats(
                total,
                attended,
                notAttended,
                previous1,
                previous2,
                others
        );
    }
}
