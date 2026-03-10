package com.foryourlife.admin.crm.report.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.report.domain.ReportViewRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.trainer.trainerDashboard.infrastructure.persistence.TrainerFocusViewRepositoryImpl;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JPAReportViewRepository implements ReportViewRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final TrainerFocusViewRepositoryImpl trainerFocusViewRepository;
    private final CallRepository callRepository;

    public JPAReportViewRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, TrainerFocusViewRepositoryImpl trainerFocusViewRepository, CallRepository callRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.trainerFocusViewRepository = trainerFocusViewRepository;
        this.callRepository = callRepository;
    }

    @Override
    public ByteArrayOutputStream generateReportView(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("El entrenamiento no existe", List.of())
        );

        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        try (Workbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Asistencia");

            int fridayAttendanceCount = (int) attendances.stream()
                    .filter(a -> a.getFridayAttendance() == AttendanceStatus.ASISTIO)
                    .count();

            int saturdayAttendanceCount = (int) attendances.stream()
                    .filter(a -> a.getSaturdayAttendance() == AttendanceStatus.ASISTIO)
                    .count();

            int sundayAttendanceCount = (int) attendances.stream()
                    .filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO)
                    .count();

            int prom = (fridayAttendanceCount + saturdayAttendanceCount + sundayAttendanceCount) / 3;
            int range1 = (int) (prom * 0.8);
            int range2 = (int) (prom * 0.65);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Día");
            headerRow.createCell(1).setCellValue("Asistencia");
            headerRow.createCell(2).setCellValue("Rango1");
            headerRow.createCell(3).setCellValue("Rango2");

            int[] values = { fridayAttendanceCount, saturdayAttendanceCount, sundayAttendanceCount };
            int[] rango1Values = { range1, range1, range1 };
            int[] rango2Values = { range2, range2, range2 };

            String[] labels = { "Viernes", "Sábado", "Domingo" };

            for (int i = 0; i < 3; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(labels[i]);
                row.createCell(1).setCellValue(values[i]);
                row.createCell(2).setCellValue(rango1Values[i]);
                row.createCell(3).setCellValue(rango2Values[i]);
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();

            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 0, 15, 20);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Asistencia " + training.getName() + " " + training.getCourseLevelDisplay());
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            XDDFDataSource<String> dayData = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(1, 3, 0, 0)
            );

            XDDFNumericalDataSource<Double> asistenciaValues = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(1, 3, 1, 1)
            );

            XDDFNumericalDataSource<Double> rango1Data = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(1, 3, 2, 2)
            );

            XDDFNumericalDataSource<Double> rango2Data = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(1, 3, 3, 3)
            );

            // === Crear gráfico de barras ===
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.BOTTOM);

            XDDFChartData barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);

            XDDFChartData.Series barSeries = barData.addSeries(dayData, asistenciaValues);
            barSeries.setTitle("Asistencia", null);
            chart.plot(barData);

            // === Crear líneas ===
            XDDFChartData lineData = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            XDDFChartData.Series line1 = lineData.addSeries(dayData, rango1Data);
            line1.setTitle("Rango1", null);

            XDDFChartData.Series line2 = lineData.addSeries(dayData, rango2Data);
            line2.setTitle("Rango2", null);

            chart.plot(lineData);


            XSSFSheet operativeSheet = (XSSFSheet) workbook.createSheet("Datos");

            var calls = callRepository.findAllByTrainingId(trainingId);

            List<CallLog> logs = new ArrayList<>();

            calls.forEach(call -> logs.addAll(call.getCallLogs()));

            logs.sort(
                    Comparator.comparing(CallLog::getDate)
                            .thenComparing(log -> log.getType().getValue())
                            .thenComparing(log -> log.getStatus().getValue())
            );

            Row headerRows = operativeSheet.createRow(0);

            headerRows.createCell(0).setCellValue("Fecha de Llamada");
            headerRows.createCell(1).setCellValue("Usuario que Llama");
            headerRows.createCell(2).setCellValue("Tipo de Llamada");
            headerRows.createCell(3).setCellValue("Estado de Llamada");
            headerRows.createCell(4).setCellValue("Notas");

            var ref = new Object() {
                int rowIdx = 1;
            };

            logs.forEach(log -> {
                Row row = operativeSheet.createRow(ref.rowIdx);
                row.createCell(0).setCellValue(log.getDate().toString());
                row.createCell(1).setCellValue(log.getCalledBy().getName());
                row.createCell(2).setCellValue(log.getType().getValue());
                row.createCell(3).setCellValue(log.getStatus().getValue());
                row.createCell(4).setCellValue(log.getNotes());
                ref.rowIdx++;
            });
            ref.rowIdx++;

            List<String> userIds = attendances.stream()
                    .map(a -> a.getUser().getId())
                    .distinct()
                    .toList();
            var participants = training.getOriginalTeam() != null ? training.getOriginalTeam().getUsers()
            .stream()
                    .collect(Collectors.toMap(
                            p -> p.getUser().getId(),
                            p -> p
                    )) : new HashMap<String, Participant>();

            var payments = trainerFocusViewRepository.buildPaymentDashboard(attendances, participants);

            Row paymentHeader = operativeSheet.createRow(ref.rowIdx);
            paymentHeader.createCell(0).setCellValue("Staff");
            paymentHeader.createCell(1).setCellValue("Pago Total Your");
            paymentHeader.createCell(2).setCellValue("Pago Parcial Your");
            paymentHeader.createCell(3).setCellValue("Pago Total Life");
            paymentHeader.createCell(4).setCellValue("Pago Parcial Life");
            paymentHeader.createCell(5).setCellValue("Pagos Parciales");
            paymentHeader.createCell(6).setCellValue("Pagos Totales");
            ref.rowIdx++;

            payments.forEach(payment -> {
                Row row = operativeSheet.createRow(ref.rowIdx);
                row.createCell(0).setCellValue(payment.getStaffName());
                row.createCell(1).setCellValue(payment.getYourPaymentsSunday());
                row.createCell(2).setCellValue(payment.getYourPlusLifePaymentsSunday());
                row.createCell(3).setCellValue(payment.getTotalPaymentsSunday());
                row.createCell(4).setCellValue(payment.getPassPercentageFinalSunday());
                row.createCell(5).setCellValue(payment.getYourPaymentsFinal());
                row.createCell(6).setCellValue(payment.getYourPlusLifePaymentsFinal());
                ref.rowIdx++;
            });

            ref.rowIdx++;
            var master = training.getOriginalTeam() != null ? training.getOriginalTeam().getMasterLife() : new ArrayList<>();
            if (!master.isEmpty()){
                Row promiseHeader = operativeSheet.createRow(ref.rowIdx);
                promiseHeader.createCell(0).setCellValue("Declaraciones de Pago Life Master");
                Row promiseColumns = operativeSheet.createRow(ref.rowIdx);
                promiseColumns.createCell(0).setCellValue("Rol");
                promiseColumns.createCell(1).setCellValue("Usuario");
                promiseColumns.createCell(2).setCellValue("Viernes");
                promiseColumns.createCell(3).setCellValue("Sábado");
                promiseColumns.createCell(4).setCellValue("Domingo");
                ref.rowIdx++;

                var promises = promiseRepository.findByTrainingId(trainingId);
                promises.forEach(promise -> {
                    Row row = operativeSheet.createRow(ref.rowIdx);
                    var user = promise.getUser();
                    boolean isMaster = false;

                    if (user != null && user.getEntityMap() != null) {
                        isMaster = user.getEntityMap().stream()
                                .map(UserEntities::getEntity)
                                .anyMatch(entity -> Objects.equals(entity, UserType.MASTER_LIFE.getValue()));
                    }

                    row.createCell(0).setCellValue(isMaster ? "Master Life" : "Participante");
                    row.createCell(1).setCellValue(promise.getUser().getName());
                    row.createCell(2).setCellValue(promise.getFirstPromise());
                    row.createCell(3).setCellValue(promise.getSecondPromise());
                    row.createCell(4).setCellValue(promise.getThirdPromise());
                    ref.rowIdx++;
                });

            }
            operativeSheet.createRow(ref.rowIdx);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
