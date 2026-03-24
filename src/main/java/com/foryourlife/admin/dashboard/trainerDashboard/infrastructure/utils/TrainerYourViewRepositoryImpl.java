package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.PaymentYourDashboard;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.TrainerYourView;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.TrainerYourViewRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerYourViewRepositoryImpl implements TrainerYourViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final TrainingRepository trainingRepository;
    private final PaymentRepository paymentRepository;
    private final OrganizationChartRepository organizationChartRepository;
    private final TeamRepository teamRepository;

    public TrainerYourViewRepositoryImpl(AttendanceRepository attendanceRepository, TrainingDashboardUtils trainingDashboardUtils, TrainingRepository trainingRepository, PaymentRepository paymentRepository, OrganizationChartRepository organizationChartRepository, TeamRepository teamRepository) {
        this.attendanceRepository = attendanceRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.trainingRepository = trainingRepository;
        this.paymentRepository = paymentRepository;
        this.organizationChartRepository = organizationChartRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    @Override
    public TrainerYourView getTrainerYourViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        Map<String, Participant> participants = trainingDashboardUtils.loadParticipants(attendances);

        if (attendances.isEmpty()) {
            throw new BaseException("No attendances found for the given training ID", List.of());
        }

        var lingererStats = trainingDashboardUtils.buildLingererStats(attendances.getFirst().getTraining(), attendances, participants);

        var previousTrainingStats = trainingDashboardUtils.buildPreviousTrainingStats(
                attendances.getFirst().getTraining(),
                attendances,
                participants
        );

        var training = attendances.getFirst().getTraining();
        var team = training.getOriginalTeam();

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        return new TrainerYourView(
                trainerName,
                attendances.getFirst().getTraining().getName(),
                attendances.getFirst().getTraining().getStartDate().toString() + " - " + attendances.getFirst().getTraining().getEndDate().toString(),
                trainingDashboardUtils.buildGeneralAttendance(attendances, participants),
                buildPaymentYourDashboard(attendances, participants),
                lingererStats,
                trainingDashboardUtils.buildNextTrainingAttendance(attendances),
                previousTrainingStats,
                trainingDashboardUtils.buildYourRecoveryPaymentStats(attendances.getFirst().getTraining(), attendances, participants)
        );
    }

    public List<PaymentYourDashboard> buildPaymentYourDashboard(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        if (attendances.isEmpty()) return List.of();

        var training = attendances.getFirst().getTraining();

        LocalDate saturday = training.getEndDate().minusDays(1);
        LocalDate sunday = training.getEndDate();

        Training pastTraining = trainingRepository
                .findByNextLevel_Id(training.getId())
                .orElse(null);

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

        List<PaymentYourDashboard> dashboards = new ArrayList<>();

        for (ChartNode staffNode : staffNodes) {

            List<String> staffParticipantIds =
                    staffToParticipants.getOrDefault(staffNode.getId(), List.of());

            List<Payment> allPayments = staffParticipantIds.stream()
                    .flatMap(pId -> paymentsByParticipantId
                            .getOrDefault(pId, List.of())
                            .stream())
                    .filter(p -> p.getTraining() != null)
                    .filter(p -> p.getTraining().getCourseLevel() == CourseLevel.LIFE)
                    .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                    .toList();

            // -------- PAGOS PREVIOS --------
            int previousLifePayments = 0;

            if (pastTraining != null) {

                previousLifePayments = (int) allPayments.stream()
                        .filter(p -> p.getTraining().getId().equals(pastTraining.getId()))
                        .count();
            }

            int totalLifePayments = allPayments.size();

            double previousPercentage = totalLifePayments == 0
                    ? 0
                    : ((double) previousLifePayments / totalLifePayments) * 100;


            // -------- SABADO --------
            int saturdayPayments = (int) allPayments.stream()
                    .filter(p -> p.getCreatedDate().toLocalDate().isBefore(saturday))
                    .count();

            int accumulatedSaturday = previousLifePayments + saturdayPayments;

            int totalYour = (int) attendances.stream()
                    .filter(a -> {
                        Participant p = participants.get(a.getUser().getId());
                        return p != null && !Boolean.TRUE.equals(p.getIsLingerer());
                    })
                    .count();

            double passSaturday = (double) accumulatedSaturday / totalYour;

            // -------- DOMINGO --------
            int sundayPayments = (int) allPayments.stream()
                    .filter(p -> p.getCreatedDate().toLocalDate().isBefore(sunday))
                    .count();

            int accumulatedSunday = accumulatedSaturday + sundayPayments;

            double passSunday = (double) accumulatedSunday / totalYour;

            dashboards.add(
                    new PaymentYourDashboard(
                            staffNode.getMembers().getName(),
                            previousLifePayments,
                            previousPercentage,
                            saturdayPayments,
                            accumulatedSaturday,
                            passSaturday,
                            sundayPayments,
                            accumulatedSunday,
                            passSunday
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
