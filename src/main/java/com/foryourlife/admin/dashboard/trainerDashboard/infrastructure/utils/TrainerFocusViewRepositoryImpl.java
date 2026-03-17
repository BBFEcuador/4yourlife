package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.LifeWeekendAssistant;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.PaymentFocusDashboard;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.TrainerFocusView;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.TrainerFocusViewRepository;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
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

import java.time.LocalDate;
import java.util.*;
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

        return new TrainerFocusView(
                trainingDashboardUtils.buildGeneralAttendance(attendances, participants),
                trainingDashboardUtils.buildGenderDashboard(attendances, participants),
                trainingDashboardUtils.buildAgeDashboard(attendances, participants),
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
                    new PaymentFocusDashboard(
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

}
