package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.bank.domain.Bank;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JPACashDrawerRepository implements CashDrawerRepository {
    private final JPAImplCashDrawerRepository repository;
    private final JPACriteriaConverter<CashDrawer> criteriaConverter;
    private final CashDrawerDetailQueryService cashDrawerDetailQueryService;
    private final InvitationRepository invitationRepository;

    public JPACashDrawerRepository(JPAImplCashDrawerRepository repository, JPACriteriaConverter<CashDrawer> criteriaConverter, CashDrawerDetailQueryService cashDrawerDetailQueryService, InvitationRepository invitationRepository) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
        this.cashDrawerDetailQueryService = cashDrawerDetailQueryService;
        this.invitationRepository = invitationRepository;
    }

    @Override
    public CashDrawer save(CashDrawer cashDrawer) {
        return this.repository.save(cashDrawer);
    }

    @Override
    public Optional<CashDrawer> getById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    @Override
    public Page<CashDrawer> getAll(Criteria criteria, Pageable pageable) {
        return this.repository.findAll(
                criteriaConverter.getJpaSpecifications(criteria),
                pageable
        );
    }

    @Override
    public Optional<CashDrawer> findByCashBoxIdAndStatus(String cashBoxId, CashDrawerStatus status) {
        return repository.findByStatusIsAndCashBox_Id(status, cashBoxId);
    }

    @Override
    public Page<CashDrawer> getByCashBoxId(Pageable pageable, String id) {
        return this.repository.findAllByCashBox_Id(id, pageable);
    }

    @Override
    public Optional<CashDrawer> findByCashBoxIdAndStatusAndUserId(String cashBoxId, CashDrawerStatus status, String userId) {
        return this.repository.findByStatusIsAndCashBox_IdAndOpenedByUser_Id(status, cashBoxId, userId);
    }

    @Override
    public Optional<CashDrawer> findByStatusIsNotAndOpenedByUserId(CashDrawerStatus cashDrawerStatus, String userId) {
        return this.repository.findAllByStatusIsNotAndOpenedByUser_Id(cashDrawerStatus, userId);
    }

    @Override
    public Optional<CashDrawer> findByStatusIsAndOpenedByUserId(CashDrawerStatus cashDrawerStatus, String userId) {
        return this.repository.findAllByStatusIsAndOpenedByUser_Id(cashDrawerStatus, userId);
    }

    @Override
    public String generatePdfReport(CashDrawer cashDrawer) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        var details = cashDrawerDetailQueryService.getByCashDrawerId(cashDrawer.getId());

        Map<String, PaymentMethodSummary> paymentMethodMap = getStringPaymentMethodSummaryMap(details);

        PaymentMethodSummary[] paymentMethods = paymentMethodMap.values().toArray(new PaymentMethodSummary[0]);

        BigDecimal totalIncome = Arrays.stream(paymentMethods)
                .map(PaymentMethodSummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Map<String, Object>> simplifiedDetails = details.stream()
                .flatMap(detail -> {
                    Map<String, Object> map = new HashMap<>();
                    PaymentHistory paymentHistory = findPaymentHistory(detail);
                    if (paymentHistory == null) {
                        return Stream.empty();
                    }
                    map.put("date", paymentHistory.getDate());
                    map.put("productName", detail.getPayment().getProducts().getFirst().getName() + "-" + detail.getPayment().getParticipant().getUser().getName() + " - ABONO");
                    map.put("paymentMethod", paymentHistory.getPaymentMethod().getType());
                    map.put("amount", paymentHistory.getAmount());
                    return Stream.of(map);
                })
                .toList();

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();

        context.setVariable("cashDrawer", cashDrawer);
        context.setVariable("details", simplifiedDetails);
        context.setVariable("paymentMethods", paymentMethods);
        context.setVariable("totalIncome", totalIncome);
        return templateEngine.process("templates/Report-cash-drawer-pdf", context);
    }

    @NotNull
    private static Map<String, PaymentMethodSummary> getStringPaymentMethodSummaryMap(List<CashDrawerDetail> details) {
        Map<String, PaymentMethodSummary> paymentMethodMap = new HashMap<>();
        List<String> paymentId = new ArrayList<>();
        for (var detail : details) {
            if (paymentId.contains(detail.getPayment().getId())) {
                continue;
            } else {
                paymentId.add(detail.getPayment().getId());
            }
            for (var paymentHistory : detail.getPayment().getPaymentshistory()) {
                String paymentMethodId = paymentHistory.getPaymentMethod().getId();
                PaymentMethodSummary summary = paymentMethodMap.computeIfAbsent(paymentMethodId, k -> new PaymentMethodSummary(paymentHistory.getPaymentMethod()));

                summary.addPayment(paymentHistory.getAmount());
            }
        }
        return paymentMethodMap;
    }

    private PaymentHistory findPaymentHistory(CashDrawerDetail detail) {
        if (detail.getPayment() != null && detail.getPaymentHistoryId() != null) {
            return detail.getPayment().getPaymentshistory().stream()
                    .filter(ph -> ph.getId().equals(detail.getPaymentHistoryId()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public ByteArrayOutputStream generateExcelReport(CashDrawer cashDrawer) {
        var details = cashDrawerDetailQueryService.getByCashDrawerId(cashDrawer.getId());

        Map<String, PaymentMethodSummary> paymentMethodMap = getStringPaymentMethodSummaryMap(details);
        PaymentMethodSummary[] paymentMethods = paymentMethodMap.values().toArray(new PaymentMethodSummary[0]);

        BigDecimal totalIncome = Arrays.stream(paymentMethods)
                .map(PaymentMethodSummary::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ── Columnas de pago dinámicas ─────────────────────────────────────────
        // Se extraen los nombres únicos de métodos de pago en el orden en que aparecen
        List<String> paymentMethodNames = details.stream()
                .map(this::findPaymentHistory)
                .filter(Objects::nonNull)
                .map(ph -> ph.getPaymentMethod().getType().toUpperCase())
                .distinct()
                .toList();

        // Índice de columna para cada método de pago (empieza en col 8)
        final int PAYMENT_COL_START = 8;
        Map<String, Integer> paymentColIndex = new LinkedHashMap<>();
        for (int i = 0; i < paymentMethodNames.size(); i++) {
            paymentColIndex.put(paymentMethodNames.get(i), PAYMENT_COL_START + i);
        }

        // Columna FACTURA siempre al final
        int facturaCol = PAYMENT_COL_START + paymentMethodNames.size();
        int totalCols = facturaCol + 1;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("CIERRE CAJA");

            // ── Estilos ───────────────────────────────────────────────────────────

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 12);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setWrapText(true);
            setBorders(headerStyle);

            CellStyle dataStyle = workbook.createCellStyle();
            setBorders(dataStyle);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            setBorders(numberStyle);

            CellStyle totalStyle = workbook.createCellStyle();
            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);
            totalStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            setBorders(totalStyle);

            CellStyle totalLabelStyle = workbook.createCellStyle();
            totalLabelStyle.setFont(totalFont);
            setBorders(totalLabelStyle);

            // ── Fila 0: Empresa ───────────────────────────────────────────────────
            Row row0 = sheet.createRow(0);
            Cell companyCell = row0.createCell(0);
            companyCell.setCellValue("IMPETUS SAS");
            companyCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalCols - 1));

            // ── Fila 1: Título cierre caja ────────────────────────────────────────
            Row row1 = sheet.createRow(1);
            Cell titleCell = row1.createCell(0);
            String locationName = cashDrawer.getCashBox().getStore().getAddress() != null
                    ? cashDrawer.getCashBox().getStore().getAddress() : "";
            titleCell.setCellValue("CIERRE CAJA " + locationName.toUpperCase() + " DETALLE: " + cashDrawer.getDetail());
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, totalCols - 1));

            // ── Fila 2: Rango de fechas ───────────────────────────────────────────
            Row row2 = sheet.createRow(2);
            Cell dateCell = row2.createCell(0);

            DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("dd 'DE' MMMM 'DE' yyyy", new Locale("es", "ES"));

            String dateRange = "DEL " + cashDrawer.getCreatedAt().format(monthFmt)
                    + " AL " + cashDrawer.getCloseDate().format(monthFmt);

            dateCell.setCellValue(dateRange.toUpperCase());
            dateCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, totalCols - 1));

            // ── Fila 3: Nombres de persona que abre la caja ────────────────────────────────────
            String OpenerNames = cashDrawer.getOpenedByUser().getName();

            Row row3 = sheet.createRow(3);
            Cell ec = row3.createCell(1);
            ec.setCellValue(OpenerNames.toUpperCase());
            CellStyle es = workbook.createCellStyle();
            Font ef = workbook.createFont();
            ef.setBold(true);
            es.setFont(ef);
            es.setAlignment(HorizontalAlignment.CENTER);
            ec.setCellStyle(es);


            // ── Fila 4: Encabezados fijos + dinámicos + FACTURA ───────────────────
            String[] fixedHeaders = {
                    "N°", "NOMBRE ENROLADOR", "TIPO", "ENROLLADO",
                    "BANCO", "TARJETA", "TIPO", "NUMERO/AUTORIZACION"
            };

            Row headerRow = sheet.createRow(4);
            headerRow.setHeightInPoints(40);

            for (int i = 0; i < fixedHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fixedHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            // Encabezados dinámicos: uno por cada método de pago encontrado
            for (Map.Entry<String, Integer> entry : paymentColIndex.entrySet()) {
                Cell cell = headerRow.createCell(entry.getValue());
                cell.setCellValue(entry.getKey());
                cell.setCellStyle(headerStyle);
            }
            // FACTURA siempre al final
            Cell facturaHeader = headerRow.createCell(facturaCol);
            facturaHeader.setCellValue("FACTURA");
            facturaHeader.setCellStyle(headerStyle);

            // ── Filas de datos ────────────────────────────────────────────────────
            int dataStartRow = 5;
            int rowNum = dataStartRow;
            int seq = 1;
            int maxDataRows = 18;

            for (int i = 0; i < maxDataRows; i++) {
                Row dataRow = sheet.createRow(rowNum + i);
                for (int c = 0; c < totalCols; c++) {
                    dataRow.createCell(c).setCellStyle(dataStyle);
                }
            }

            // ── Una sola query para todos los enroladores ─────────────────────────
            // Recolecta tokens únicos de invitación (manteniendo orden de aparición)
            Set<String> tokens = details.stream()
                    .map(d -> d.getPayment().getParticipant())
                    .filter(Objects::nonNull)
                    .map(Participant::getInvitationToken)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            // Si no hay tokens, evita llamar al repositorio con una lista vacía
            Map<String, String> enrollerNameByToken;
            if (tokens.isEmpty()) {
                enrollerNameByToken = Collections.emptyMap();
            } else {
                List<String> tokenList = new ArrayList<>(tokens);
                enrollerNameByToken = invitationRepository.findAllByTokenIn(tokenList)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(inv -> inv.getToken() != null && inv.getEnrolled() != null && inv.getEnrolled().getName() != null)
                        .collect(Collectors.toMap(
                                Invitation::getToken,
                                inv -> inv.getEnrolled().getName()
                        ));
            }
            for (CashDrawerDetail detail : details) {
                PaymentHistory ph = findPaymentHistory(detail);
                if (ph == null) continue;

                Row dataRow = sheet.getRow(rowNum);
                rowNum++;

                Payment payment = detail.getPayment();
                String enrollerName = Optional.ofNullable(payment.getParticipant())
                        .map(Participant::getInvitationToken)
                        .map(enrollerNameByToken::get)
                        .orElse("");
                String type = payment.getParticipant() != null ? "Participante" : "ML";
                String enrolledName = payment.getParticipant() != null
                        ? payment.getParticipant().getUser().getName() : "";
                String authNumber = ph.getTransactionId() != null ? ph.getTransactionId() : "";
                BigDecimal amount = ph.getAmount();
                String methodType = ph.getPaymentMethod().getType().toUpperCase();

                String bankName = Optional.ofNullable(ph.getPaymentMethod())
                        .map(PaymentMethod::getBank)
                        .map(Bank::getName)
                        .orElse("");

                setCellValue(dataRow, 0, seq++, dataStyle);
                setCellValue(dataRow, 1, enrollerName, dataStyle);
                setCellValue(dataRow, 2, type, dataStyle);
                setCellValue(dataRow, 3, enrolledName, dataStyle);
                setCellValue(dataRow, 4, bankName, dataStyle);
                setCellValue(dataRow, 5, ph.getPaymentMethod().getType() != null
                        ? ph.getPaymentMethod().getType() : "", dataStyle);
                setCellValue(dataRow, 6, "", dataStyle);
                setCellValue(dataRow, 7, authNumber, dataStyle);

                // ── Monto en la columna dinámica correspondiente ──────────────────
                Integer targetCol = paymentColIndex.get(methodType);
                if (targetCol != null) {
                    setCellNumeric(dataRow, targetCol, amount, numberStyle);
                }
            }

            // ── Fila de TOTALES ───────────────────────────────────────────────────
            int totalsRowIndex = dataStartRow + maxDataRows + 1;
            Row totalsRow = sheet.createRow(totalsRowIndex);

            Cell totalsLabel = totalsRow.createCell(0);
            totalsLabel.setCellValue("TOTALES");
            totalsLabel.setCellStyle(totalLabelStyle);
            sheet.addMergedRegion(new CellRangeAddress(totalsRowIndex, totalsRowIndex, 0, 7));

            // SUM dinámica por cada columna de método de pago
            for (int col : paymentColIndex.values()) {
                Cell sumCell = totalsRow.createCell(col);
                String colLetter = CellReference.convertNumToColString(col);
                sumCell.setCellFormula(String.format("SUM(%s%d:%s%d)",
                        colLetter, dataStartRow + 1,
                        colLetter, dataStartRow + maxDataRows));
                sumCell.setCellStyle(totalStyle);
            }

            // ── Fila gran total ───────────────────────────────────────────────────
            int grandTotalRowIndex = totalsRowIndex + 1;
            Row grandTotalRow = sheet.createRow(grandTotalRowIndex);
            Cell grandTotalCell = grandTotalRow.createCell(PAYMENT_COL_START);

            // Suma de todas las columnas de pago dinámicas
            String formula = paymentColIndex.values().stream()
                    .map(col -> CellReference.convertNumToColString(col) + (totalsRowIndex + 1))
                    .collect(Collectors.joining("+"));
            grandTotalCell.setCellFormula(formula);
            grandTotalCell.setCellStyle(totalStyle);

            // ── Anchos de columna ─────────────────────────────────────────────────
            int[] fixedWidths = {6, 20, 14, 20, 14, 20, 22, 28};
            for (int i = 0; i < fixedWidths.length; i++) {
                sheet.setColumnWidth(i, fixedWidths[i] * 256);
            }
            for (int col = PAYMENT_COL_START; col <= facturaCol; col++) {
                sheet.setColumnWidth(col, 16 * 256);
            }

            // ── Escritura al stream ───────────────────────────────────────────────
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el reporte Excel de cierre de caja", e);
        }
    }

// ── Helpers privados ──────────────────────────────────────────────────────────

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private void setCellValue(Row row, int col, Object value, CellStyle style) {
        Cell cell = row.getCell(col);
        if (cell == null) cell = row.createCell(col);
        if (value instanceof Number n) {
            cell.setCellValue(n.doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private void setCellNumeric(Row row, int col, BigDecimal value, CellStyle style) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) return;
        Cell cell = row.getCell(col);
        if (cell == null) cell = row.createCell(col);
        cell.setCellValue(value.doubleValue());
        cell.setCellStyle(style);
    }
}