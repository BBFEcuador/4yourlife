package com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure.persistence;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your.FocusResumeDashboard;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your.TrainerYourView;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.your.TrainerYourViewRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TrainerYourViewRepositoryImpl implements TrainerYourViewRepository {
    private final AttendanceRepository attendanceRepository;
    private final TrainerFocusViewRepositoryImpl trainerFocusViewRepository;
    private final TrainingRepository trainingRepository;

    public TrainerYourViewRepositoryImpl(AttendanceRepository attendanceRepository, TrainerFocusViewRepositoryImpl trainerFocusViewRepository, TrainingRepository trainingRepository) {
        this.attendanceRepository = attendanceRepository;
        this.trainerFocusViewRepository = trainerFocusViewRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public TrainerYourView getTrainerYourViewByTrainingId(String trainingId) {
        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        Map<String, Participant> participants = trainerFocusViewRepository.loadParticipants(attendances);

        if(attendances.isEmpty()) {
            throw new BaseException("No attendances found for the given training ID", List.of());
        }

        var focusTraining = trainingRepository.findByNextLevel_Id(attendances.getFirst().getTraining().getId()).orElseThrow(
                () -> new BaseException("Training not found for next level", List.of())
        );

        var focusAttendance = attendanceRepository.findAttendanceByTraining(focusTraining.getId());

        var focusParticipants = trainerFocusViewRepository.loadParticipants(focusAttendance);

        return new TrainerYourView(
                trainerFocusViewRepository.buildGeneralAttendance(attendances, participants),
                buildFocusResumeDashboard(focusAttendance, focusParticipants),
                trainerFocusViewRepository.buildPaymentDashboard(attendances, participants)
        );
    }

    public FocusResumeDashboard buildFocusResumeDashboard(List<Attendance> attendances, Map<String, Participant> participants) {
        var paymentsDashboard = trainerFocusViewRepository.buildPaymentDashboard(attendances, participants);

        AtomicInteger yourPayment = new AtomicInteger();
        AtomicInteger lifePayment = new AtomicInteger();

        paymentsDashboard.forEach(paymentDashboard -> {
            yourPayment.addAndGet(paymentDashboard.getYourCompletedPaymentsCount());
            lifePayment.addAndGet(paymentDashboard.getLifeCompletedPaymentsCount());
        });

        return new FocusResumeDashboard(
                attendances.size(),
                yourPayment.get(),
                lifePayment.get(),
                yourPayment.get() + lifePayment.get()
        );
    }
}
