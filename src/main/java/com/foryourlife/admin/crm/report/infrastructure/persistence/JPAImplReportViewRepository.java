package com.foryourlife.admin.crm.report.infrastructure.persistence;

import com.foryourlife.admin.crm.report.domain.ReportView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAImplReportViewRepository extends JpaRepository<ReportView, String> {
}
