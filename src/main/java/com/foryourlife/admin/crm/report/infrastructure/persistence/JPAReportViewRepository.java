package com.foryourlife.admin.crm.report.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.report.domain.ReportViewRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class JPAReportViewRepository implements ReportViewRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final CallRepository callRepository;

    public JPAReportViewRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, CallRepository callRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
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

            calls.forEach(call -> {
//               var
            });
            Row crmHeader = operativeSheet.createRow(0);

            crmHeader.createCell(0).setCellValue("ID Llamada");
            // === Guardar ===
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
