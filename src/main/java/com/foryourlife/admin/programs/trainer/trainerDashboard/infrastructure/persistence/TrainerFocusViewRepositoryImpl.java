package com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure.persistence;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.*;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrainerFocusViewRepositoryImpl implements TrainerFocusViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl;
    private final ParticipantRepository participantRepository;
    private final OrganizationChartRepository organizationChartRepository;
    private final PaymentRepository paymentRepository;

    public TrainerFocusViewRepositoryImpl(AttendanceRepository attendanceRepository, TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl, ParticipantRepository participantRepository, OrganizationChartRepository organizationChartRepository, PaymentRepository paymentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.trainerLifeViewRepositoryImpl = trainerLifeViewRepositoryImpl;
        this.participantRepository = participantRepository;
        this.organizationChartRepository = organizationChartRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public TrainerFocusView getTrainerFocusViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        Map<String, Participant> participants = loadParticipants(attendances);

        return new TrainerFocusView(
                this.buildGeneralAttendance(attendances, participants),
                this.buildGenderDashboard(attendances, participants),
                this.buildAgeDashboard(attendances, participants),
                this.buildPaymentDashboard(attendances, participants)
        );
    }


    public GeneralAttendance buildGeneralAttendance(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {
        List<UserAttendance> userAttendances = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {
                    String entity = trainerLifeViewRepositoryImpl.resolveUserType(a.getUser());
                    UserType userType = UserType.fromValue(entity);

                    return new UserAttendance(
                            a.getUser().getName(),
                            userType,
                            a.getFridayAttendance(),
                            a.getSaturdayAttendance(),
                            a.getSundayAttendance()
                    );
                })
                .toList();

        int totalLingerers = (int) attendances.stream()
                .filter(a -> {
                    Participant p = participants.get(a.getUser().getId());
                    return p != null && p.getIsLingerer();
                })
                .count();

        int totalFocus = (int) attendances.stream()
                .filter(a -> {
                    Participant p = participants.get(a.getUser().getId());
                    return p != null && !p.getIsLingerer();
                })
                .count();

        return new GeneralAttendance(totalLingerers, totalFocus, userAttendances);
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
                        count.apply(Attendance::getFridayAttendance, "F").intValue()
                ),
                new GenderDashboard("Sábado",
                        count.apply(Attendance::getSaturdayAttendance, "H").intValue(),
                        count.apply(Attendance::getSaturdayAttendance, "F").intValue()
                ),
                new GenderDashboard("Domingo",
                        count.apply(Attendance::getSundayAttendance, "H").intValue(),
                        count.apply(Attendance::getSundayAttendance, "F").intValue()
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
                            Integer age = calculateAge(LocalDate.ofInstant(p.getProfile().getBirthday().toInstant(), java.time.ZoneId.systemDefault()));
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
        friday.age_less_18   = countByAgeRange.apply(Attendance::getFridayAttendance, "less_18").intValue();
        friday.age_18_27     = countByAgeRange.apply(Attendance::getFridayAttendance, "18_27").intValue();
        friday.age_28_40     = countByAgeRange.apply(Attendance::getFridayAttendance, "28_40").intValue();
        friday.age_41_65     = countByAgeRange.apply(Attendance::getFridayAttendance, "41_65").intValue();
        friday.age_above_65  = countByAgeRange.apply(Attendance::getFridayAttendance, "above_65").intValue();

        AgeDashboard saturday = new AgeDashboard();
        saturday.day = "Sábado";
        saturday.age_less_18   = countByAgeRange.apply(Attendance::getSaturdayAttendance, "less_18").intValue();
        saturday.age_18_27     = countByAgeRange.apply(Attendance::getSaturdayAttendance, "18_27").intValue();
        saturday.age_28_40     = countByAgeRange.apply(Attendance::getSaturdayAttendance, "28_40").intValue();
        saturday.age_41_65     = countByAgeRange.apply(Attendance::getSaturdayAttendance, "41_65").intValue();
        saturday.age_above_65  = countByAgeRange.apply(Attendance::getSaturdayAttendance, "above_65").intValue();

        AgeDashboard sunday = new AgeDashboard();
        sunday.day = "Domingo";
        sunday.age_less_18   = countByAgeRange.apply(Attendance::getSundayAttendance, "less_18").intValue();
        sunday.age_18_27     = countByAgeRange.apply(Attendance::getSundayAttendance, "18_27").intValue();
        sunday.age_28_40     = countByAgeRange.apply(Attendance::getSundayAttendance, "28_40").intValue();
        sunday.age_41_65     = countByAgeRange.apply(Attendance::getSundayAttendance, "41_65").intValue();
        sunday.age_above_65  = countByAgeRange.apply(Attendance::getSundayAttendance, "above_65").intValue();

        return List.of(friday, saturday, sunday);
    }


    private Map<String, Participant> loadParticipants(List<Attendance> attendances) {
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

        var organizationChart = organizationChartRepository
                .getOrganizationChartTrainingId(attendances.getFirst().getTraining().getId());

        if (organizationChart.isEmpty()) return List.of();

        List<ChartNode> allNodes = flattenTree(organizationChart.get().getNodes());

        List<ChartNode> staffNodes = allNodes.stream()
                .filter(n -> n.getLevel() == UserType.STAFF)
                .toList();

        List<ChartNode> participantNodes = allNodes.stream()
                .filter(n -> n.getLevel() == UserType.PARTICIPANT)
                .toList();


        Map<String, List<String>> staffToParticipants = participantNodes.stream()
                .collect(Collectors.groupingBy(
                        ChartNode::getParentNodeId,
                        Collectors.mapping(n -> n.getMembers().getId(), Collectors.toList())
                ));

        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        Map<String, List<Payment>> paymentsByParticipantId = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getParticipant().getUser().getId()));

        List<PaymentDashboard> dashboards = new ArrayList<>();

        for (ChartNode staffNode : staffNodes) {

            List<String> staffParticipantIds =
                    staffToParticipants.getOrDefault(staffNode.getId(), List.of());

            List<Payment> allPayments = staffParticipantIds.stream()
                    .flatMap(pId -> paymentsByParticipantId.getOrDefault(pId, List.of()).stream())
                    .toList();

            long completed = allPayments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                    .count();

            long partial = allPayments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                    .count();

            long total = allPayments.size();

            long yourCompleted = countByCourseLevel(allPayments, CourseLevel.YOUR, true);
            long yourPartial   = countByCourseLevel(allPayments, CourseLevel.YOUR, false);

            long lifeCompleted = countByCourseLevel(allPayments, CourseLevel.LIFE, true);
            long lifePartial   = countByCourseLevel(allPayments, CourseLevel.LIFE, false);


            dashboards.add(new PaymentDashboard(
                    staffNode.getMembers().getName(),
                    (int) completed,
                    (int) partial,
                    (int) total,
                    (int) yourPartial,
                    (int) yourCompleted,
                    (int) lifePartial,
                    (int) lifeCompleted
            ));
        }

        return dashboards;
    }

    private long countByCourseLevel(List<Payment> payments, CourseLevel level, boolean completed) {
        return payments.stream()
                .filter(p -> (completed && p.getStatus() == PaymentStatus.COMPLETED)
                        || (!completed && p.getStatus() == PaymentStatus.PENDING))
                .filter(p -> p.getProducts() != null && p.getProducts().stream()
                        .anyMatch(prod -> prod.getPrograms() != null &&
                                prod.getPrograms().stream().anyMatch(prg ->
                                        prg.getCourseLevel() == level
                                )
                        )
                )
                .count();
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

}
