package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.*;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImplOperativeAssistantDashboardRepository implements OperativeAssistantDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final CallRepository callRepository;
    private final PaymentRepository paymentRepository;
    private final TeamRepository teamRepository;

    public ImplOperativeAssistantDashboardRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, CallRepository callRepository, PaymentRepository paymentRepository, TeamRepository teamRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.callRepository = callRepository;
        this.paymentRepository = paymentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public OperativeAssistantDashboard getOpAssistDashboardByTeamId(String teamId) {
        var team = teamRepository.findById(teamId).orElseThrow(
                () -> new BaseException("Team not found", List.of("Team with id " + teamId + " not found"))
        );
        var training = team.getTraining();

        Training focus = null, your = null, life1 = null, life2 = null, life3 = null;

        if (training.getCourseLevel().equals(CourseLevel.YOUR)) {
            focus = training;
        }
        if (training.getCourseLevel().equals(CourseLevel.YOUR)) {
            your = training;
            focus = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
            your = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            your = (life1 != null) ? trainingRepository.findByNextLevel_Id(life1.getId()).orElse(null) : null;
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? trainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
            your = (life1 != null) ? trainingRepository.findByNextLevel_Id(life1.getId()).orElse(null) : null;
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        }

        List<TrainingInfo> trainingInfos = new ArrayList<>();
        if (focus != null) trainingInfos.add(buildOperativeAssistantDashboardSection(focus));
        if (your != null) trainingInfos.add(buildOperativeAssistantDashboardSection(your));
        if (life1 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life1));
        if (life2 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life2));
        if (life3 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life3));
        return new OperativeAssistantDashboard(
                trainingInfos
        );
    }

    @Override
    public ByteArrayOutputStream generateReport(String teamId) {

        OperativeAssistantDashboard dashboard = this.getOpAssistDashboardByTeamId(teamId);

        try (Workbook workbook = new XSSFWorkbook()) {

            // ---- ESTILOS ----
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Estilo para celdas normales
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // Estilo para porcentajes
            CellStyle percentStyle = workbook.createCellStyle();
            percentStyle.cloneStyleFrom(cellStyle);
            percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));

            // Estilo para números
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.cloneStyleFrom(cellStyle);
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));


            // ============================
            //  HOJA 1: TRAINING INFO
            // ============================
            Sheet trainingSheet = workbook.createSheet("Info Entrenamiento");
            int rowIndex = 0;

            String[] trainingHeaders = {
                    "Nivel", "Nombre del Entrenador", "Nombre del Equipo", "Numero del Equipo",
                    "Participantes totales", "Participantes reales", "Declaraciones de Participantes",
                    "Master Lifes totales", "Master Lifes reales", "Declaraciones de Master Lifes",
                    "Total Enrolados", "Total Enrolados rales", "Declaraciones de Enrolados"
            };

            Row headerRow = trainingSheet.createRow(rowIndex++);
            for (int i = 0; i < trainingHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(trainingHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            for (TrainingInfo t : dashboard.getTrainingInfo()) {
                Row row = trainingSheet.createRow(rowIndex++);
                int c = 0;

                row.createCell(c).setCellValue(t.getCourseLevel().name());
                row.getCell(c++).setCellStyle(cellStyle);

                row.createCell(c).setCellValue(t.getTrainerName());
                row.getCell(c++).setCellStyle(cellStyle);

                row.createCell(c).setCellValue(t.getTeamName());
                row.getCell(c++).setCellStyle(cellStyle);

                row.createCell(c).setCellValue(t.getTeamNumber());
                row.getCell(c++).setCellStyle(cellStyle);

                int[] numbers = {
                        t.getTotalParticipants(),
                        t.getTotalParticipantAssistants(),
                        t.getTotalParticipantsDeclarations(),
                        t.getTotalMasterLifes(),
                        t.getTotalMasterLifesAssistants(),
                        t.getTotalMasterLifesDeclarations(),
                        t.getTotalEnrolments(),
                        t.getTotalEnrolmentsAssistants(),
                        t.getTotalEnrolmentsDeclarations()
                };

                for (int num : numbers) {
                    Cell cell = row.createCell(c++);
                    cell.setCellValue(num);
                    cell.setCellStyle(numberStyle);
                }
            }

            // AUTOSIZE
            for (int i = 0; i < trainingHeaders.length; i++) {
                trainingSheet.autoSizeColumn(i);
            }


            // ============================
            //  HOJA 2: CALLS
            // ============================
            Sheet callSheet = workbook.createSheet("Llamadas");
            rowIndex = 0;

            String[] callHeaders = {"Nivel", "Tipo de Llamada", "Estado", "Total de llamadas"};

            Row callHeader = callSheet.createRow(rowIndex++);
            for (int i = 0; i < callHeaders.length; i++) {
                Cell cell = callHeader.createCell(i);
                cell.setCellValue(callHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            for (TrainingInfo t : dashboard.getTrainingInfo()) {
                for (CallTypeStats stats : t.getCallsInfoList()) {
                    for (CallsInfo info : stats.getStatuses()) {

                        Row row = callSheet.createRow(rowIndex++);
                        int c = 0;

                        row.createCell(c).setCellValue(t.getCourseLevel().name());
                        row.getCell(c++).setCellStyle(cellStyle);

                        row.createCell(c).setCellValue(stats.getCallType().name());
                        row.getCell(c++).setCellStyle(cellStyle);

                        row.createCell(c).setCellValue(info.getStatus().name());
                        row.getCell(c++).setCellStyle(cellStyle);

                        Cell totalCell = row.createCell(c);
                        totalCell.setCellValue(info.getTotalCalls());
                        totalCell.setCellStyle(numberStyle);
                    }
                }
            }

            for (int i = 0; i < callHeaders.length; i++) {
                callSheet.autoSizeColumn(i);
            }


            // ============================
            //  HOJA 3: WEEKLY PAYMENTS
            // ============================
            Sheet paymentsSheet = workbook.createSheet("Pagos Semanales");
            rowIndex = 0;

            String[] payHeaders = {
                    "Nivel", "Numero de seman", "Dia de la semana",
                    "Participantes", "Pagos Your", "Pagos Your+Life",
                    "Total de pagos", "Pagos parciales", "Porcentaje aprobado", "Porcentaje Proyectado"
            };

            Row payHeader = paymentsSheet.createRow(rowIndex++);
            for (int i = 0; i < payHeaders.length; i++) {
                Cell cell = payHeader.createCell(i);
                cell.setCellValue(payHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            for (TrainingInfo t : dashboard.getTrainingInfo()) {
                for (WeeklyPaymentStats week : t.getWeeklyPaymentStatsList()) {
                    for (DayOfWeek day : week.getStatsPerDay().keySet()) {

                        DailyStats d = week.getStatsPerDay().get(day);
                        Row row = paymentsSheet.createRow(rowIndex++);
                        int c = 0;

                        row.createCell(c).setCellValue(t.getCourseLevel().name());
                        row.getCell(c++).setCellStyle(cellStyle);

                        Cell weekCell = row.createCell(c);
                        weekCell.setCellValue(week.getWeekNumber());
                        weekCell.setCellStyle(numberStyle);
                        c++;
                        String dia = day.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                        dia = Character.toUpperCase(dia.charAt(0)) + dia.substring(1);
                        row.createCell(c).setCellValue(dia);
                        row.getCell(c++).setCellStyle(cellStyle);

                        int[] numberValues = {
                                d.getParticipantsFinal(),
                                d.getYourCount(),
                                d.getYourLifeCount(),
                                d.getTotalPayments(),
                                d.getPartialPayments()
                        };

                        for (int num : numberValues) {
                            Cell numCell = row.createCell(c++);
                            numCell.setCellValue(num);
                            numCell.setCellStyle(numberStyle);
                        }

                        Cell pass = row.createCell(c++);
                        pass.setCellValue(d.getPassPercent());
                        pass.setCellStyle(percentStyle);

                        Cell projected = row.createCell(c++);
                        projected.setCellValue(d.getProjectedPercent());
                        projected.setCellStyle(percentStyle);
                    }
                }
            }

            for (int i = 0; i < payHeaders.length; i++) {
                paymentsSheet.autoSizeColumn(i);
            }

            // ============================
            //  HOJA 4: SUMMARY
            // ============================
            Sheet summarySheet = workbook.createSheet("Resumen");
            int summaryRow = 0;

            // Encabezados del resumen
            String[] summaryHeaders = {
                    "Nivel",
                    "Total Participantes",
                    "Total Enrolados",
                    "Total Llamadas",
                    "Llamadas Completadas",
                    "Llamadas No Contestadas",
                    "Llamadas Reprogramadas",
                    "Total Pagos",
                    "Pagos Completados",
                    "Pagos Parciales",
                    "% Aprobado (Prom)",
                    "% Proyectado (Prom)"
            };

            Row summaryHeaderRow = summarySheet.createRow(summaryRow++);
            for (int i = 0; i < summaryHeaders.length; i++) {
                Cell cell = summaryHeaderRow.createCell(i);
                cell.setCellValue(summaryHeaders[i]);
                cell.setCellStyle(headerStyle);
            }


            // AGRUPAR INFORMACIÓN
            for (TrainingInfo t : dashboard.getTrainingInfo()) {

                int totalParticipants = t.getTotalParticipants();
                int totalEnrolments = t.getTotalEnrolments();

                // Count calls
                int totalCalls = 0;
                int doneCalls = 0;
                int notAnsweredCalls = 0;
                int rescheduledCalls = 0;

                for (CallTypeStats callTypeStats : t.getCallsInfoList()) {
                    for (CallsInfo info : callTypeStats.getStatuses()) {
                        totalCalls += info.getTotalCalls();

                        switch (info.getStatus()) {
                            case DONE -> doneCalls += info.getTotalCalls();
                            case NOT_ANSWERED -> notAnsweredCalls += info.getTotalCalls();
                            case RE_SCHEDULED -> rescheduledCalls += info.getTotalCalls();
                        }
                    }
                }

                // Payments
                int totalPayments = 0;
                int completedPayments = 0;
                int partialPayments = 0;

                double avgPass = 0;
                double avgProjected = 0;
                int dayCount = 0;

                for (WeeklyPaymentStats week : t.getWeeklyPaymentStatsList()) {
                    for (DailyStats d : week.getStatsPerDay().values()) {

                        totalPayments += d.getTotalPayments();
                        partialPayments += d.getPartialPayments();
                        completedPayments += (d.getTotalPayments() - d.getPartialPayments());

                        avgPass += d.getPassPercent();
                        avgProjected += d.getProjectedPercent();
                        dayCount++;
                    }
                }

                if (dayCount > 0) {
                    avgPass /= dayCount;
                    avgProjected /= dayCount;
                }

                // ROW DEL RESUMEN
                Row row = summarySheet.createRow(summaryRow++);
                int c = 0;

                row.createCell(c).setCellValue(t.getCourseLevel().name());
                row.getCell(c++).setCellStyle(cellStyle);

                Cell p = row.createCell(c++);
                p.setCellValue(totalParticipants);
                p.setCellStyle(numberStyle);

                Cell e = row.createCell(c++);
                e.setCellValue(totalEnrolments);
                e.setCellStyle(numberStyle);

                // Calls
                Cell tc = row.createCell(c++);
                tc.setCellValue(totalCalls);
                tc.setCellStyle(numberStyle);

                Cell dc = row.createCell(c++);
                dc.setCellValue(doneCalls);
                dc.setCellStyle(numberStyle);

                Cell ncc = row.createCell(c++);
                ncc.setCellValue(notAnsweredCalls);
                ncc.setCellStyle(numberStyle);

                Cell rc = row.createCell(c++);
                rc.setCellValue(rescheduledCalls);
                rc.setCellStyle(numberStyle);

                // Payments
                Cell tp = row.createCell(c++);
                tp.setCellValue(totalPayments);
                tp.setCellStyle(numberStyle);

                Cell comp = row.createCell(c++);
                comp.setCellValue(completedPayments);
                comp.setCellStyle(numberStyle);

                Cell part = row.createCell(c++);
                part.setCellValue(partialPayments);
                part.setCellStyle(numberStyle);

                // Percentages
                Cell passPercentCell = row.createCell(c++);
                passPercentCell.setCellValue(avgPass);
                passPercentCell.setCellStyle(percentStyle);

                Cell projectedPercentCell = row.createCell(c++);
                projectedPercentCell.setCellValue(avgProjected);
                projectedPercentCell.setCellStyle(percentStyle);
            }

            // Autosize columnas del resumen
            for (int i = 0; i < summaryHeaders.length; i++) {
                summarySheet.autoSizeColumn(i);
            }
            // ---- OUTPUT ----
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;

        } catch (Exception e) {
            throw new BaseException("Error generating report", List.of(e.getMessage()));
        }
    }


    public TrainingInfo buildOperativeAssistantDashboardSection(Training training) {
        var attendances = attendanceRepository.findAttendanceByTraining(training.getId());

        int participantDeclarations = 0, masterLifesDeclarations = 0, enrolmentsDeclarations;
        if (training.getCourseLevel() != CourseLevel.YOUR) {
            var declarations = promiseRepository.findByTrainingId(training.getId());

            if (!declarations.isEmpty()) {
                for (var declaration : declarations) {
                    var participant = training.getOriginalTeam().getUsers().stream().filter(user -> user.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                    if (participant.isPresent()) {
                        participantDeclarations += declaration.getThirdPromise();
                    } else {
                        var masterLife = training.getOriginalTeam().getMasterLife().stream().filter(ml -> ml.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                        if (masterLife.isPresent()) {
                            masterLifesDeclarations += declaration.getThirdPromise();
                        }
                    }
                }
            }
        }

        enrolmentsDeclarations = participantDeclarations + masterLifesDeclarations;

        Map<CallType, Map<CallStatus, CallsInfo>> callsByType = new HashMap<>();

        for (CallType type : CallType.values()) {

            Map<CallStatus, CallsInfo> statusMap = new HashMap<>();

            for (CallStatus status : CallStatus.values()) {
                CallsInfo info = new CallsInfo();
                info.setStatus(status);
                info.setTotalCalls(0);
                statusMap.put(status, info);
            }

            callsByType.put(type, statusMap);
        }

        callRepository.findAllByTrainingId(training.getId())
                .forEach(call -> call.getCallLogs().forEach(callLog -> {

                    CallType type = callLog.getType();
                    CallStatus status = callLog.getStatus();

                    CallsInfo info = callsByType.get(type).get(status);
                    info.setTotalCalls(info.getTotalCalls() + 1);
                }));

        List<CallTypeStats> finalList = new ArrayList<>();

        for (CallType type : callsByType.keySet()) {
            CallTypeStats stats = new CallTypeStats();
            stats.setCallType(type);
            stats.setStatuses(new ArrayList<>(callsByType.get(type).values()));
            finalList.add(stats);
        }


        var participantAttendances = attendances.stream()
                .filter(attendance -> training.getOriginalTeam().getUsers().stream()
                        .anyMatch(participant -> participant.getUser().getId().equals(attendance.getUser().getId()))
                ).count();
        var masterLifeAttendances = attendances.stream()
                .filter(attendance -> training.getOriginalTeam().getMasterLife().stream()
                        .anyMatch(ml -> ml.getUser().getId().equals(attendance.getUser().getId()))
                ).count();
        List<WeeklyPaymentStats> weeklyTable;
        weeklyTable = buildWeeklyTable(training);
        return new TrainingInfo(
                training.getCourseLevel(),
                training.getOriginalTeam().getTrainer().getName(),
                training.getOriginalTeam().getName(),
                training.getOriginalTeam().getTrainingNumber().toString(),
                training.getOriginalTeam().getUsers().size(),
                (int) participantAttendances,
                participantDeclarations,
                training.getOriginalTeam().getMasterLife().size(),
                (int) masterLifeAttendances,
                masterLifesDeclarations,
                training.getOriginalTeam().getUsers().size() + training.getOriginalTeam().getMasterLife().size(),
                (int) (participantAttendances + masterLifeAttendances),
                enrolmentsDeclarations,
                finalList,
                weeklyTable
        );
    }

    private List<LocalDate> getFridayBoundaries(LocalDate start, LocalDate nextFriday) {
        List<LocalDate> fridays = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(nextFriday)) {
            fridays.add(current);
            current = current.plusWeeks(1);
        }

        return fridays;
    }

    public List<WeeklyPaymentStats> buildWeeklyTable(Training training) {

        LocalDate startFriday = training.getStartDate();
        LocalDate nextFriday = training.getNextLevel().getStartDate();

        List<LocalDate> fridayBoundaries = getFridayBoundaries(startFriday, nextFriday);

        List<Payment> payments = new java.util.ArrayList<>(List.of());
        training.getOriginalTeam().getUsers().forEach(participant ->
                paymentRepository.findAllBetweenDates(training.getStartDate().atStartOfDay(), training.getNextLevel().getStartDate().atStartOfDay()).forEach(
                        payment -> {
                            if (payment.getParticipant().getId().equals(participant.getId())) {
                                payments.add(payment);
                            }
                        }
                )
        );

        Map<LocalDate, List<Payment>> paymentsByDay = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getCreatedDate().toLocalDate()));

        List<WeeklyPaymentStats> weeklyStats = new ArrayList<>();

        for (int i = 0; i < fridayBoundaries.size() - 1; i++) {

            LocalDate weekStart = fridayBoundaries.get(i);
            LocalDate weekEnd = fridayBoundaries.get(i + 1); // exclusivo

            WeeklyPaymentStats weekStats = new WeeklyPaymentStats();
            weekStats.setWeekNumber(i + 1);

            Map<DayOfWeek, DailyStats> dayStatsMap = new EnumMap<>(DayOfWeek.class);

            LocalDate date = weekStart;

            while (date.isBefore(weekEnd)) {

                List<Payment> dayPayments = paymentsByDay.getOrDefault(date, List.of());

                DailyStats daily = calculateDailyStats(dayPayments);
                dayStatsMap.put(date.getDayOfWeek(), daily);

                date = date.plusDays(1);
            }

            weekStats.setStatsPerDay(dayStatsMap);
            weeklyStats.add(weekStats);
        }

        return weeklyStats;
    }

    private DailyStats calculateDailyStats(List<Payment> payments) {

        DailyStats stats = new DailyStats();

        stats.setParticipantsFinal(
                (int) payments.stream()
                        .map(p -> p.getParticipant().getId())
                        .distinct()
                        .count()
        );

        stats.setYourCount(
                (int) payments.stream()
                        .filter(p -> p.getParticipant().getParticipantLevel().getCourseLevel() == CourseLevel.YOUR)
                        .count()
        );

        stats.setYourLifeCount(
                (int) payments.stream()
                        .filter(p -> {
                            var level = p.getParticipant().getParticipantLevel().getCourseLevel();
                            return level == CourseLevel.YOUR || level == CourseLevel.LIFE;
                        })
                        .count()
        );

        stats.setTotalPayments(payments.size());

        stats.setPartialPayments(
                (int) payments.stream()
                        .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                        .count()
        );

        if (stats.getParticipantsFinal() > 0) {
            stats.setPassPercent(
                    (stats.getYourLifeCount() * 100.0) / stats.getParticipantsFinal()
            );
        } else {
            stats.setPassPercent(0.0);
        }

        if (stats.getParticipantsFinal() > 0) {
            stats.setProjectedPercent(
                    ((stats.getYourLifeCount() + stats.getPartialPayments()) * 100.0)
                            / stats.getParticipantsFinal()
            );
        } else {
            stats.setProjectedPercent(0.0);
        }

        return stats;
    }

}
