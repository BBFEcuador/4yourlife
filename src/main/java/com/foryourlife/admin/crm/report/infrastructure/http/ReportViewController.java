package com.foryourlife.admin.crm.report.infrastructure.http;

import com.foryourlife.admin.crm.report.application.ReportCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportViewController {
    @Autowired
    private ReportCommandService reportCommandService;

    @PostMapping("generate")
    public ResponseEntity<byte[]> generateReport() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );
        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=.xlsx"
        );

        byte[] excelBytes = reportCommandService.generateReport("").toByteArray();

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excelBytes);
    }
}
