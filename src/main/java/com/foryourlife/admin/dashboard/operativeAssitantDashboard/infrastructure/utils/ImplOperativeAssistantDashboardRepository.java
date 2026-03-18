package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.*;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
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
    @Transactional
    public OperativeAssistantDashboard getOpAssistDashboardByTeamId(String trainingId) {
        return (OperativeAssistantDashboard) List.of();
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
            headerStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
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

                row.createCell(c).setCellValue(t.getCourseLevel().getValue().toUpperCase());
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
            // Estilo para secciones (Nivel)
            CellStyle levelStyle = workbook.createCellStyle();
            levelStyle.cloneStyleFrom(headerStyle);
            levelStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
            levelStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo para subtítulos (Tipo de llamada)
            CellStyle subLevelStyle = workbook.createCellStyle();
            subLevelStyle.cloneStyleFrom(headerStyle);
            subLevelStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            subLevelStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Sheet callSheet = workbook.createSheet("Llamadas");
            rowIndex = 0;

            String[] callHeaders = {"Nivel", "Tipo de Llamada", "Estado", "Total de llamadas"};

            // ENCABEZADO
            Row callHeader = callSheet.createRow(rowIndex++);
            for (int i = 0; i < callHeaders.length; i++) {
                Cell cell = callHeader.createCell(i);
                cell.setCellValue(callHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            for (TrainingInfo t : dashboard.getTrainingInfo()) {

                // ➤ SEPARADOR POR NIVEL
                Row levelRow = callSheet.createRow(rowIndex++);
                Cell levelCell = levelRow.createCell(0);
                levelCell.setCellValue("Nivel: " + t.getCourseLevel().getValue().toUpperCase());
                levelCell.setCellStyle(levelStyle);  // ← estilo especial para secciones
                callSheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));

                for (CallTypeStats stats : t.getCallsInfoList()) {

                    // SUB-SEPARADOR POR TIPO DE LLAMADA
                    Row typeRow = callSheet.createRow(rowIndex++);
                    Cell typeCell = typeRow.createCell(0);
                    typeCell.setCellValue("Tipo de llamada: " + stats.getCallType().getValue());
                    typeCell.setCellStyle(subLevelStyle);
                    callSheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));

                    // DETALLE POR ESTADO
                    for (CallsInfo info : stats.getStatuses()) {

                        Row row = callSheet.createRow(rowIndex++);
                        int c = 0;

                        // Nivel
                        row.createCell(c).setCellValue(t.getCourseLevel().getValue());
                        row.getCell(c++).setCellStyle(cellStyle);

                        // Tipo de llamada
                        row.createCell(c).setCellValue(stats.getCallType().getValue());
                        row.getCell(c++).setCellStyle(cellStyle);

                        // Estado
                        String estado = info.getStatus().getValue();
                        row.createCell(c).setCellValue(estado);
                        row.getCell(c++).setCellStyle(cellStyle);

                        // Total
                        Cell totalCell = row.createCell(c);
                        totalCell.setCellValue(info.getTotalCalls());
                        totalCell.setCellStyle(numberStyle);
                    }
                }
            }

            // Autosize
            for (int i = 0; i < callHeaders.length; i++) {
                callSheet.autoSizeColumn(i);
            }


            // ============================
            //  HOJA 3: WEEKLY PAYMENTS
            // ============================
            Sheet paymentsSheet = workbook.createSheet("Pagos Semanales");
            int rowIndexPayment = 0;

            // Estilo para títulos grandes
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            // Estilo para subtítulos (semana)
            CellStyle subtitleStyle = workbook.createCellStyle();
            Font subtitleFont = workbook.createFont();
            subtitleFont.setBold(true);
            subtitleFont.setFontHeightInPoints((short) 12);
            subtitleStyle.setFont(subtitleFont);

            // Encabezados
            String[] payHeaders = {
                    "Día", "Participantes", "Pagos Your", "Pagos Your+Life",
                    "Total de pagos", "Pagos parciales", "% aprobado", "% proyectado"
            };

            // Recorremos por nivel
            for (TrainingInfo t : dashboard.getTrainingInfo()) {

                // ====================
                // TÍTULO DEL NIVEL
                // ====================
                Row levelRow = paymentsSheet.createRow(rowIndexPayment++);
                Cell levelCell = levelRow.createCell(0);
                levelCell.setCellValue("NIVEL: " + t.getCourseLevel().getValue().toUpperCase());
                levelCell.setCellStyle(titleStyle);

                rowIndexPayment++; // línea en blanco

                // Recorremos las semanas
                for (WeeklyPaymentStats week : t.getWeeklyPaymentStatsList()) {

                    // ======================
                    // SUBTÍTULO DE SEMANA
                    // ======================
                    Row weekRow = paymentsSheet.createRow(rowIndexPayment++);
                    Cell weekCell = weekRow.createCell(0);
                    weekCell.setCellValue("Semana " + week.getWeekNumber());
                    weekCell.setCellStyle(subtitleStyle);

                    // Encabezado de la tabla
                    Row headerRowPayment = paymentsSheet.createRow(rowIndexPayment++);
                    for (int i = 0; i < payHeaders.length; i++) {
                        Cell cell = headerRowPayment.createCell(i);
                        cell.setCellValue(payHeaders[i]);
                        cell.setCellStyle(headerStyle);
                    }

                    // ======================
                    // FILAS POR DÍA
                    // ======================
                    for (DayOfWeek day : week.getStatsPerDay().keySet()) {

                        DailyStats d = week.getStatsPerDay().get(day);

                        Row row = paymentsSheet.createRow(rowIndexPayment++);
                        int c = 0;

                        // Día en español
                        String dia = day.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                        dia = Character.toUpperCase(dia.charAt(0)) + dia.substring(1);
                        row.createCell(c++).setCellValue(dia);

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

                        Cell passCell = row.createCell(c++);
                        passCell.setCellValue(d.getPassPercent());
                        passCell.setCellStyle(percentStyle);

                        Cell projCell = row.createCell(c++);
                        projCell.setCellValue(d.getProjectedPercent());
                        projCell.setCellStyle(percentStyle);
                    }

                    rowIndexPayment++; // espacio entre semanas
                }

                rowIndexPayment++; // espacio entre niveles
            }

            // Autosize
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

}
