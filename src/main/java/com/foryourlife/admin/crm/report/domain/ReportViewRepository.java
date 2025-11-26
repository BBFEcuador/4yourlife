package com.foryourlife.admin.crm.report.domain;

import java.io.ByteArrayOutputStream;

public interface ReportViewRepository {
    ByteArrayOutputStream generateReportView(String trainingId);
}
