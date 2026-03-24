package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboardRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourPayments;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ImplOperativeYourDashboard implements OperativeYourDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;

    public ImplOperativeYourDashboard(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public OperativeYourDashboard findByTrainingId(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("No se encontró el entrenamiento", List.of("El id " + trainingId + " no corresponde a ningún entrenamiento"))
        );

        if (!training.getCourseLevel().equals(CourseLevel.YOUR)) {
            throw new BaseException("El entrenamiento no es de nivel YOUR", List.of("El entrenamiento con id " + trainingId + " no es de nivel YOUR, por lo tanto no se puede mostrar en el dashboard de operativo asistente"));
        }

        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        var participants = trainingDashboardUtils.loadParticipants(attendances);

        var attendanceDashboard = trainingDashboardUtils.buildGeneralAttendance(attendances, participants);

        var team = training.getOriginalTeam();

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        return new OperativeYourDashboard(
                training.getName(),
                trainerName,
                attendanceDashboard,
                buildOperativeYourPayments(attendances, participants)
        );
    }

    public OperativeYourPayments buildOperativeYourPayments(List<Attendance> attendances, Map<String, Participant> participants) {
        if (attendances.isEmpty()) return null;

        var training = attendances.getFirst().getTraining();

        LocalDate saturday = training.getEndDate().minusDays(1);
        LocalDate sunday = training.getEndDate();

        Training pastTraining = trainingRepository
                .findByNextLevel_Id(training.getId())
                .orElse(null);

        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        int previousLifePayments = 0;

        if (pastTraining != null) {
            previousLifePayments = (int) payments.stream()
                    .filter(p -> p.getTraining().getId().equals(pastTraining.getId()))
                    .count();
        }

        int saturdayPayments = (int) payments.stream()
                .filter(p -> p.getCreatedDate().toLocalDate().isEqual(saturday))
                .count();

        int sundayPayments = (int) payments.stream()
                .filter(p -> p.getCreatedDate().toLocalDate().isEqual(sunday))
                .count();

        int totalPayments = saturdayPayments + sundayPayments + previousLifePayments;

        double totalPaymentsPercentage = (double) totalPayments / participants.size() * 100;

        return new OperativeYourPayments(
                previousLifePayments,
                saturdayPayments,
                sundayPayments,
                totalPayments,
                totalPaymentsPercentage
        );
    }
}
