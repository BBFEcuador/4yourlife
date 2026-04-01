package com.foryourlife.admin.crm.statement.application;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementDTO;
import com.foryourlife.admin.crm.statement.domain.StatementDtoStatusEnum;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.charts.chartNodes.domain.ChartNode;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChart;
import com.foryourlife.admin.programs.charts.organizationChart.domain.OrganizationChartRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatementQueryService {
    private final StatementRepository statementRepository;
    private final OrganizationChartRepository organizationChartRepository;
    private final PaymentRepository paymentRepository;
    private final InvitationRepository invitationRepository;
    private final AttendanceRepository attendanceRepository;

    public StatementQueryService(StatementRepository statementRepository, OrganizationChartRepository organizationChartRepository, PaymentRepository paymentRepository, InvitationRepository invitationRepository, AttendanceRepository attendanceRepository) {
        this.statementRepository = statementRepository;
        this.organizationChartRepository = organizationChartRepository;
        this.paymentRepository = paymentRepository;
        this.invitationRepository = invitationRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public Page<StatementDTO> findAll(Pageable pageable, Criteria criteria, String trainingId) {

        criteria.filters.add(new Filter(
                "id", trainingId, "training",
                Filter.Operation.EQUAL,
                Filter.LogicalOperator.AND
        ));

        Page<Statement> statementsPage = statementRepository.findAll(pageable, criteria);

        var statements = statementsPage.getContent();

        var participantIds = statements.stream()
                .filter(s -> s.getParticipant() != null)
                .map(s -> s.getParticipant().getId())
                .toList();

        var tokens = statements.stream()
                .filter(s -> s.getParticipant() != null)
                .map(s -> s.getParticipant().getInvitationToken())
                .filter(Objects::nonNull)
                .toList();

        Map<String, List<Payment>> paymentsByParticipant =
                paymentRepository.findAllByParticipantIds(participantIds)
                        .stream()
                        .collect(Collectors.groupingBy(p -> p.getParticipant().getId()));

        Map<String, List<Attendance>> attendancesByUserId =
                attendanceRepository.findAttendanceByTraining(trainingId)
                        .stream()
                        .collect(Collectors.groupingBy(a -> a.getUser().getId()));

        var organizationChart = organizationChartRepository
                .getOrganizationChartTrainingId(trainingId)
                .orElse(null);

        Map<String, ChartNode> nodeByUserId;
        Map<String, ChartNode> nodeById;

        if (organizationChart != null) {
            nodeByUserId = organizationChart.getNodes().stream()
                    .filter(n -> n.getMembers() != null)
                    .collect(Collectors.toMap(n -> n.getMembers().getId(), n -> n));

            nodeById = organizationChart.getNodes().stream()
                    .collect(Collectors.toMap(ChartNode::getId, n -> n));
        } else {
            nodeByUserId = Collections.emptyMap();
            nodeById = Collections.emptyMap();
        }

        Map<String, Invitation> invitationByToken =
                invitationRepository.findAllByTokenIn(tokens)
                        .stream()
                        .collect(Collectors.toMap(Invitation::getToken, i -> i));

        return statementsPage.map(statement ->
                toDTO(statement, nodeByUserId, nodeById, paymentsByParticipant, invitationByToken, attendancesByUserId)
        );
    }

    public List<Statement> findAllByTrainingId(String trainingId) {
        return statementRepository.findByTrainingId(trainingId);
    }

    public Statement findById(String id) {
        return statementRepository.findById(id).orElse(null);
    }

    private StatementDTO toDTO(
            Statement statement,
            Map<String, ChartNode> nodeByUserId,
            Map<String, ChartNode> nodeById,
            Map<String, List<Payment>> paymentsByParticipant,
            Map<String, Invitation> invitationByToken,
            Map<String, List<Attendance>> attendancesByUser
    ) {

        var participant = statement.getParticipant();
        if (participant == null) return null;

        var user = participant.getUser();
        String userId = user.getId();

        var participantNode = nodeByUserId.get(userId);

        String staffName = "Sin staff";
        if (participantNode != null) {
            var parentNode = nodeById.get(participantNode.getParentNodeId());
            if (parentNode != null && parentNode.getMembers() != null) {
                staffName = parentNode.getMembers().getName();
            }
        }

        List<Payment> participantPayments =
                paymentsByParticipant.getOrDefault(participant.getId(), List.of());

        Optional<Attendance> participantAttendance = attendancesByUser.getOrDefault(userId, List.of()).stream().findFirst();

        AttendanceStatus attendanceStatus = null;

        if (participantAttendance.isPresent()){
            attendanceStatus = participantAttendance.get().getSundayAttendance();
        }

        StatementDtoStatusEnum yourPaymentStatus =
                resolvePaymentStatus(participantPayments, CourseLevel.YOUR);

        StatementDtoStatusEnum lifePaymentStatus =
                resolvePaymentStatus(participantPayments, CourseLevel.LIFE);

        String enrollmentTeam = "Sin equipo";
        String enrollerName = "Sin enrolador";

        var invitation = invitationByToken.get(participant.getInvitationToken());
        if (invitation != null && invitation.getEnrolled() != null) {
            enrollmentTeam = invitation.getEnrolled().getTrainingName();
            enrollerName = invitation.getEnrolled().getName();
        }

        return new StatementDTO(
                staffName,
                participant.getUser().getName(),
                statement,
                yourPaymentStatus,
                lifePaymentStatus,
                user.getNickname(),
                enrollmentTeam,
                enrollerName,
                statement.getComment(),
                attendanceStatus
        );
    }

    private StatementDtoStatusEnum resolvePaymentStatus(List<Payment> payments, CourseLevel level) {

        boolean hasPending = false;

        for (Payment p : payments) {

            if (p.getProducts() == null) continue;

            boolean matchesLevel = p.getProducts().stream()
                    .anyMatch(product ->
                            product.getPrograms() != null &&
                                    product.getPrograms().stream()
                                            .anyMatch(program -> level.equals(program.getCourseLevel()))
                    );

            if (!matchesLevel) continue;

            if (p.getStatus() == PaymentStatus.COMPLETED) {
                return StatementDtoStatusEnum.FULL_PAYMENT;
            }

            if (p.getStatus() == PaymentStatus.PENDING) {
                hasPending = true;
            }
        }

        return hasPending
                ? StatementDtoStatusEnum.PAYMENT
                : StatementDtoStatusEnum.NOT_PAYMENT;
    }
}
