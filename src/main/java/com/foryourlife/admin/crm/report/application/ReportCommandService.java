package com.foryourlife.admin.crm.report.application;

import com.foryourlife.admin.crm.report.infrastructure.persistence.JPAReportViewRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ReportCommandService {
    private final JPAReportViewRepository reportViewRepository;

    public ReportCommandService(JPAReportViewRepository reportViewRepository) {
        this.reportViewRepository = reportViewRepository;
    }

    public ByteArrayOutputStream generateReport(String trainingId) {
        return reportViewRepository.generateReportView(trainingId);
    }
}
