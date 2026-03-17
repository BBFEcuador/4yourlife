package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.common.*;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.YourRecoveryPaymentStats;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrainingDashboardUtils {

    private final AttendanceRepository attendanceRepository;
    private final ParticipantRepository participantRepository;
    private final PaymentRepository paymentRepository;

    public TrainingDashboardUtils(AttendanceRepository attendanceRepository, ParticipantRepository participantRepository, PaymentRepository paymentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.participantRepository = participantRepository;
        this.paymentRepository = paymentRepository;
    }

    public GeneralAttendance buildGeneralAttendance(
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {
        List<UserAttendance> userAttendances = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {

                    Participant participant = participants.get(a.getUser().getId());

                    String entity = this.resolveUserType(a.getUser());
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

        int initialPx = attendances.size();

        double distortionPercentage = initialPx == 0 ? 0 : ((double) totalDistorter / initialPx) * 100;
        return new GeneralAttendance(initialPx, totalFocus, totalLingerers, totalDistorter, distortionPercentage, userAttendances);
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

    public LingererStats buildLingererStats(
            Training currentTraining,
            List<Attendance> currentAttendances,
            Map<String, Participant> participants
    ) {

        int total = 0;
        int attended = 0;
        int notAttended = 0;

        LingererGroupStats previous1 = new LingererGroupStats(0,0);
        LingererGroupStats previous2 = new LingererGroupStats(0,0);
        LingererGroupStats previous3 = new LingererGroupStats(0,0);
        LingererGroupStats others = new LingererGroupStats(0,0);

        // 🔥 PRELOAD (1 sola query)
        List<String> userIds = currentAttendances.stream()
                .map(a -> a.getUser().getId())
                .distinct()
                .toList();

        Map<String, List<Attendance>> attendancesByUser =
                attendanceRepository.findAllByUserIds(userIds)
                        .stream()
                        .collect(Collectors.groupingBy(a -> a.getUser().getId()));

        for (Attendance attendance : currentAttendances) {

            String userId = attendance.getUser().getId();
            Participant participant = participants.get(userId);

            if (participant == null) continue;
            if (!Boolean.TRUE.equals(participant.getIsLingerer())) continue;

            total++;

            boolean hasAttendance = attendance.getSundayAttendance() == AttendanceStatus.ASISTIO;

            if (hasAttendance) attended++;
            else notAttended++;

            List<Attendance> userAttendances = attendancesByUser.getOrDefault(userId, List.of());

            List<Attendance> pastAttendances = userAttendances.stream()
                    .filter(a ->
                            !a.getTraining().getId().equals(currentTraining.getId()) &&
                                    a.getTraining().getEndDate().isBefore(currentTraining.getEndDate())
                    )
                    .sorted(Comparator.comparing(
                            (Attendance a) -> a.getTraining().getEndDate()
                    ).reversed())
                    .toList();

            if (pastAttendances.isEmpty()) continue;

            LingererGroupStats targetGroup;

            // 👇 AQUÍ estaba tu bug antes (siempre era 0)
            int index = 0;

            if (index == 0) targetGroup = previous1;
            else if (index == 1) targetGroup = previous2;
            else if (index == 2) targetGroup = previous3;
            else targetGroup = others;

            targetGroup.incrementTotal();

            if (hasAttendance) {
                targetGroup.incrementAttended();
            }
        }

        return new LingererStats(
                total,
                attended,
                notAttended,
                previous1,
                previous2,
                previous3,
                others
        );
    }

    public NextTrainingAttendance buildNextTrainingAttendance(List<Attendance> currentAttendances) {

        var nextTrainingAttendance = new NextTrainingAttendance(0, 0.0);

        var nextTrainingOpt = currentAttendances.getFirst().getTraining().getNextLevel();

        var nextTrainingAttendances = attendanceRepository.findAttendanceByTraining(nextTrainingOpt.getId());

        if (nextTrainingAttendances.isEmpty()) {
            return nextTrainingAttendance;
        }

        currentAttendances.forEach(attendance -> {
                    nextTrainingAttendances.forEach(a -> {
                        if (a.getUser().getId().equals(attendance.getUser().getId()) && a.getSundayAttendance() == AttendanceStatus.ASISTIO) {
                            nextTrainingAttendance.setNextTrainingAttendanceCount(
                                    nextTrainingAttendance.getNextTrainingAttendanceCount() + 1
                            );
                        }
                    });
                }
        );

        var pastTrainingAttendances = attendanceRepository.findAttendanceByTraining(nextTrainingOpt.getId());

        long sundayCount = pastTrainingAttendances.stream()
                .filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO)
                .count();

        double percentage = sundayCount == 0 ? 0.0 :
                (double) nextTrainingAttendance.getNextTrainingAttendanceCount() / sundayCount * 100.0;

        nextTrainingAttendance.setNextTrainingAttendancePercentage(percentage);

        return nextTrainingAttendance;
    }

    public PreviousTrainingStats buildPreviousTrainingStats(
            Training currentTraining,
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        int attendeesFromPreviousTraining = 0;
        int previousLifePayments = 0;

        for (Attendance attendance : attendances) {

            Participant participant = participants.get(attendance.getUser().getId());
            if (participant == null) continue;

            // verificar si asistió al entrenamiento actual
            boolean attended =
                    attendance.getFridayAttendance() == AttendanceStatus.ASISTIO ||
                            attendance.getSaturdayAttendance() == AttendanceStatus.ASISTIO ||
                            attendance.getSundayAttendance() == AttendanceStatus.ASISTIO;

            if (!attended) continue;

            // obtener entrenamientos pasados
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

            if (pastAttendances.isEmpty()) continue;

            // si viene del entrenamiento anterior
            Attendance previousTrainingAttendance = pastAttendances.getFirst();
            attendeesFromPreviousTraining++;

            // buscar pagos LIFE anteriores
            List<Payment> payments =
                    paymentRepository.findByAllParticipantId(participant.getId());

            boolean hasPreviousLifePayment = payments.stream()
                    .anyMatch(p ->
                            p.getTraining() != null &&
                                    p.getTraining().getCourseLevel() == CourseLevel.LIFE &&
                                    p.getStatus() == PaymentStatus.COMPLETED &&
                                    p.getCreatedDate().toLocalDate().isBefore(currentTraining.getStartDate())
                    );

            if (hasPreviousLifePayment) {
                previousLifePayments++;
            }
        }

        return new PreviousTrainingStats(
                attendeesFromPreviousTraining,
                previousLifePayments
        );
    }

    public String resolveUserType(User user) {
        if (user == null || user.getEntityMap() == null) {
            return null;
        }

        boolean isMasterLife = user.getEntityMap().stream()
                .anyMatch(e -> e.getEntity().equals(UserType.MASTER_LIFE.name()));

        boolean isParticipant = user.getEntityMap().stream()
                .anyMatch(e -> e.getEntity().equals(UserType.PARTICIPANT.name()));

        if (isMasterLife) return UserType.MASTER_LIFE.getValue();
        if (isParticipant) return UserType.PARTICIPANT.getValue();
        return null;
    }

    public YourRecoveryPaymentStats buildYourRecoveryPaymentStats(
            Training currentTraining,
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        // 🔹 obtener participantIds
        List<String> participantIds = participants.values().stream()
                .map(Participant::getId)
                .toList();

        // 🔹 traer TODOS los payments (sin filtrar)
        Map<String, List<Payment>> paymentsByParticipant =
                paymentRepository.findAllByParticipantIds(participantIds)
                        .stream()
                        .collect(Collectors.groupingBy(p -> p.getParticipant().getId()));

        int recovered = 0;
        int recoveredWithPreviousLifePayment = 0;

        for (Attendance attendance : attendances) {

            Participant participant = participants.get(attendance.getUser().getId());
            if (participant == null) continue;
            if (!Boolean.TRUE.equals(participant.getIsLingerer())) continue;

            boolean attended = attendance.getSundayAttendance() == AttendanceStatus.ASISTIO;

            if (!attended) continue;

            recovered++;

            List<Payment> payments = paymentsByParticipant
                    .getOrDefault(participant.getId(), List.of());

            boolean hasPreviousLifePayment = payments.stream()
                    .anyMatch(p -> {
                        if (p.getProducts() == null || p.getProducts().isEmpty()) return false;

                        Product product = p.getProducts().getFirst();

                        if (product.getPrograms() == null || product.getPrograms().isEmpty()) return false;

                        LocalDateTime createdAt = LocalDateTime.parse(p.getCreated_at());

                        return product.getPrograms().getFirst().getCourseLevel() == CourseLevel.LIFE
                                && p.getStatus() == PaymentStatus.COMPLETED
                                && createdAt.toLocalDate().isBefore(currentTraining.getStartDate());
                    });

            if (hasPreviousLifePayment) {
                recoveredWithPreviousLifePayment++;
            }
        }

        double percentage = recovered == 0 ? 0 :
                (double) recoveredWithPreviousLifePayment / recovered * 100;

        return new YourRecoveryPaymentStats(
                recovered,
                recoveredWithPreviousLifePayment,
                percentage
        );
    }
}
