package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CashDrawerCommandService {
    private final CashDrawerRepository repository;
    private final UserRepository userRepository;
    private final CashBoxRepository cashBoxRepository;

    public CashDrawerCommandService(CashDrawerRepository repository, UserRepository userRepository, CashBoxRepository cashBoxRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cashBoxRepository = cashBoxRepository;
    }

    public CashDrawer save(CashDrawer cashDrawer) {
        return repository.save(cashDrawer);
    }

    public ByteArrayOutputStream closeDrawer(String id, String userId) {
        var existingDrawer = repository.findByCashBoxIdAndStatus(id, CashDrawerStatus.OPEN).orElseThrow(
                () -> new BaseException("No hay cajas abiertas para el cash box", List.of(""))
        );

        if (existingDrawer.getStatus() == CashDrawerStatus.CLOSED) {
            throw new BaseException("La caja esta cerrada", List.of(""));
        }
        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );

        existingDrawer.setStatus(CashDrawerStatus.CLOSED);
        existingDrawer.setCloseDate(LocalDateTime.now());
        existingDrawer.setClosedByUser(user);
        existingDrawer.setClosedBalance(getActualBalance(existingDrawer));
        repository.save(existingDrawer);
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(repository.generatePdfReport(existingDrawer));
            renderer.layout();
            renderer.createPDF(pdf);
            String fileName = "invoice_" + existingDrawer.getId() + ".pdf";
            String filePath = Paths.get("").toAbsolutePath().toString() + File.separator + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdf.toByteArray());
            }
            return pdf;
        } catch (Exception e) {
            throw new BaseException("Error generating invoice", List.of(e.getMessage()));
        }
    }

    public CashDrawer openDrawer(String cashBoxId, String userId, Double openingBalance,String detail) {
        var cashBox = cashBoxRepository.findById(cashBoxId).orElseThrow(
                () -> new BaseException("Cash box not found", List.of(""))
        );
        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );

        var existingDrawer = repository.getByIsOpenAndByUserId(userId);

        if (existingDrawer.isPresent()) {
            throw new BaseException("La caja ya esta abierta", List.of(""));
        }

        return save(new CashDrawer(UUID.randomUUID().toString(), CashDrawerStatus.OPEN, user, null, LocalDateTime.now(), null, openingBalance, null, detail, cashBox));
    }


    public Double getActualBalance(CashDrawer cashDrawer) {
        Double totalPayments = 0.0;
        if (cashDrawer.getCashDrawerDetails()==null || cashDrawer.getCashDrawerDetails().isEmpty()){
            return cashDrawer.getOpeningBalance();
        }
        for (CashDrawerDetail detail : cashDrawer.getCashDrawerDetails()) {
            Payment payment = detail.getPayment();
            if (payment != null) {
                for (PaymentHistory paymentHistory : payment.getPaymentshistory()) {
                    if (paymentHistory.getId().equals(detail.getPaymentHistoryId())) {
                        totalPayments += paymentHistory.getAmount();
                    }
                }
            }
        }
        return cashDrawer.getOpeningBalance() + totalPayments;
    }
}
