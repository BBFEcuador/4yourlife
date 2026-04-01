package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.AgeDashboard;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.GenderDashboard;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.*;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrainerFocusViewRepositoryImpl implements TrainerFocusViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final InvitationRepository invitationRepository;
    private final OrganizationChartRepository organizationChartRepository;
    private final PaymentRepository paymentRepository;

    public TrainerFocusViewRepositoryImpl(AttendanceRepository attendanceRepository, TrainingDashboardUtils trainingDashboardUtils, InvitationRepository invitationRepository, OrganizationChartRepository organizationChartRepository, PaymentRepository paymentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.invitationRepository = invitationRepository;
        this.organizationChartRepository = organizationChartRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public TrainerFocusView getTrainerFocusViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        Map<String, Participant> participants = trainingDashboardUtils.loadParticipants(attendances);

        var lingererStats = trainingDashboardUtils.buildLingererStats(
                attendances.getFirst().getTraining(),
                attendances,
                participants
        );

        var nextTrainingAttendanceCount = trainingDashboardUtils.buildNextTrainingAttendance(attendances);

        var training = attendances.getFirst().getTraining();
        var team = training.getOriginalTeam();

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        return new TrainerFocusView(
                trainerName,
                attendances.getFirst().getTraining().getName(),
                attendances.getFirst().getTraining().getStartDate().toString() + " - " + attendances.getFirst().getTraining().getEndDate().toString(),
                trainingDashboardUtils.buildGeneralAttendance(attendances, participants),
                this.buildCityDashboard(attendances, participants),
                this.buildGenderDashboard(attendances, participants),
                this.buildAgeDashboard(attendances, participants),
                this.buildFocusPaymentDashboard(attendances, participants),
                this.buildLifeWeekendAssistants(attendances, participants),
                lingererStats,
                nextTrainingAttendanceCount
        );
    }


    public Map<String, LifeWeekendAssistant> buildLifeWeekendAssistants(List<Attendance> attendances, Map<String, Participant> participants) {

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
        return lifeWeekendAssistants;
    }

    public List<PaymentFocusDashboard> buildFocusPaymentDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        if (attendances.isEmpty()) return List.of();

        var training = attendances.getFirst().getTraining();
        LocalDate sunday = training.getEndDate();

        var organizationChart = organizationChartRepository
                .getOrganizationChartByTeamIdAndCourseLevel(training.getOriginalTeam().getId(), CourseLevel.FOCUS);

        if (organizationChart.isEmpty()) return List.of();


        List<ChartNode> staffNodes = organizationChart.get().getNodes().stream()
                .filter(n -> n.getLevel() == UserType.STAFF)
                .toList();

        List<ChartNode> participantNodes = organizationChart.get().getNodes().stream()
                .filter(n -> n.getLevel() == UserType.PARTICIPANT)
                .toList();


        Map<String, List<String>> staffToParticipants = participantNodes.stream()
                .collect(Collectors.groupingBy(
                        ChartNode::getParentNodeId,
                        Collectors.mapping(n -> n.getMembers().getId(), Collectors.toList())
                ));


        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        Map<String, List<Payment>> paymentsByParticipantId = payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getParticipant().getUser().getId()
                ));

        List<PaymentFocusDashboard> dashboards = new ArrayList<>();

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
                    .filter(p ->
                            p.getProducts() != null &&
                                    p.getProducts().stream().anyMatch(product ->
                                            product.getPrograms() != null &&
                                                    product.getPrograms().stream().anyMatch(program ->
                                                            CourseLevel.YOUR.equals(program.getCourseLevel())
                                                    )
                                    ) &&
                                    p.getStatus() == PaymentStatus.COMPLETED &&
                                    p.getCreatedDate().isBefore(training.getStartDate().atStartOfDay())
                    )
                    .count();

            int lifeSunday = (int) sundayPayments.stream()
                    .filter(p ->
                            p.getProducts() != null &&
                                    p.getProducts().stream().anyMatch(product ->
                                            product.getPrograms() != null &&
                                                    product.getPrograms().stream().anyMatch(program ->
                                                            CourseLevel.LIFE.equals(program.getCourseLevel())
                                                    )
                                    ) &&
                                    p.getStatus() == PaymentStatus.COMPLETED &&
                                    p.getCreatedDate().isBefore(training.getStartDate().atStartOfDay())
                    )
                    .count();

            int yourPlusLifeSunday = yourSunday + lifeSunday;

            int yourFinal = (int) allPayments.stream()
                    .filter(p ->
                            p.getProducts() != null &&
                                    p.getProducts().stream().anyMatch(product ->
                                            product.getPrograms() != null &&
                                                    product.getPrograms().stream().anyMatch(program ->
                                                            CourseLevel.YOUR.equals(program.getCourseLevel())
                                                    )
                                    ) &&
                                    p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int lifeFinal = (int) allPayments.stream()
                    .filter(p ->
                            p.getProducts() != null &&
                                    p.getProducts().stream().anyMatch(product ->
                                            product.getPrograms() != null &&
                                                    product.getPrograms().stream().anyMatch(program ->
                                                            CourseLevel.LIFE.equals(program.getCourseLevel())
                                                    )
                                    ) &&
                                    p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            int yourPlusLifeFinal = yourFinal + lifeFinal;

            int totalFocus = (int) attendances.stream()
                    .filter(a -> {
                        Participant p = participants.get(a.getUser().getId());
                        return p != null && !Boolean.TRUE.equals(p.getIsLingerer());
                    })
                    .count();

            double passPercentageSunday = (double) yourPlusLifeSunday / totalFocus;

            double passPercentageTotal = (double) yourPlusLifeFinal / totalFocus;


            dashboards.add(
                    new PaymentFocusDashboard(
                            staffNode.getMembers().getName(),
                            yourSunday,
                            lifeSunday,
                            yourPlusLifeSunday,
                            passPercentageSunday,
                            yourFinal,
                            lifeFinal,
                            yourPlusLifeFinal,
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

    private Integer calculateAge(LocalDate birthday) {
        if (birthday == null) return null;
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public List<CityParticipantDashboard> buildCityDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {
        Map<String, String> cityMap = participants.values().stream()
                .collect(Collectors.toMap(
                        p -> p.getUser().getId(),
                        p -> {
                            String city = p.getProfile().getCity();
                            return city == null ? "" : city.toUpperCase();
                        }
                ));

        return List.of(
                new CityParticipantDashboard("Viernes",
                        countQuito(attendances, cityMap, Attendance::getFridayAttendance, true),   // Quito
                        countQuito(attendances, cityMap, Attendance::getFridayAttendance, false)  // Otros
                ),
                new CityParticipantDashboard("Sábado",
                        countQuito(attendances, cityMap, Attendance::getSaturdayAttendance, true),
                        countQuito(attendances, cityMap, Attendance::getSaturdayAttendance, false)
                ),
                new CityParticipantDashboard("Domingo",
                        countQuito(attendances, cityMap, Attendance::getSundayAttendance, true),
                        countQuito(attendances, cityMap, Attendance::getSundayAttendance, false)
                )
        );
    }

    private int countQuito(
            List<Attendance> attendances,
            Map<String, String> cityMap,
            Function<Attendance, AttendanceStatus> dayExtractor,
            boolean onlyQuito
    ) {
        return (int) attendances.stream()
                .filter(a -> {
                    String city = cityMap.get(a.getUser().getId());
                    return onlyQuito
                            ? "QUITO".equals(city)
                            : !"QUITO".equals(city);
                })
                .filter(a -> dayExtractor.apply(a) == AttendanceStatus.ASISTIO)
                .count();
    }

}
