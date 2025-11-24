package com.foryourlife.admin.crm.report.infrastructure.persistence;

import com.foryourlife.admin.crm.report.domain.ReportViewRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class JPAReportViewRepository implements ReportViewRepository {
    private final JPAImplReportViewRepository jpaImplReportViewRepository;


    public JPAReportViewRepository(JPAImplReportViewRepository jpaImplReportViewRepository) {
        this.jpaImplReportViewRepository = jpaImplReportViewRepository;
    }

    @Override
    public ByteArrayOutputStream generateReportView(String reportId) {
        try (Workbook workbook = new XSSFWorkbook()) {

            var reportView = jpaImplReportViewRepository.findById(reportId)
                    .orElseThrow();

            Sheet sheet = workbook.createSheet("Reporte");

            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Hola Mundo");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
