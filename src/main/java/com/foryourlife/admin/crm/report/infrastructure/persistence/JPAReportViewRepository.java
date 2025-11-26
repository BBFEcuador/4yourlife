package com.foryourlife.admin.crm.report.infrastructure.persistence;

import com.foryourlife.admin.crm.report.domain.ReportViewRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class JPAReportViewRepository implements ReportViewRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;

    public JPAReportViewRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
    }

    @Override
    public ByteArrayOutputStream generateReportView(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("El entrenamiento no existe", List.of())
        );

        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Asistencia");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Asistencias al entrenamiento: " + training.getName());

            Row headerRow = sheet.createRow(2);

            String[] headers = {
                    "Viernes", "Sábado", "Domingo", "Promedio", "Rango 1 (80%)", "Rango 2 (65%)"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

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

            int[] data = {
                    fridayAttendanceCount,
                    saturdayAttendanceCount,
                    sundayAttendanceCount,
                    prom,
                    range1,
                    range2
            };

            Row dataRow = sheet.createRow(3);

            for (int i = 0; i < data.length; i++) {
                Cell cell = dataRow.createCell(i);
                cell.setCellValue(data[i]);
                cell.setCellStyle(dataStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
