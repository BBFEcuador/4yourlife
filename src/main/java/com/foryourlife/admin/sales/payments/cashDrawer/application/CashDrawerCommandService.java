package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.email.DispatchNotification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CashDrawerCommandService {
    private final CashDrawerRepository repository;
    private final UserRepository userRepository;
    private final CashBoxRepository cashBoxRepository;
    private final DispatchNotification sendgrid;

    public CashDrawerCommandService(CashDrawerRepository repository, UserRepository userRepository, CashBoxRepository cashBoxRepository, DispatchNotification sendgrid) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cashBoxRepository = cashBoxRepository;
        this.sendgrid = sendgrid;
    }

    public CashDrawer save(CashDrawer cashDrawer) {
        return repository.save(cashDrawer);
    }

    @Transactional
    public ByteArrayOutputStream closeDrawer(String id, String userId) {

        var existingDrawer = repository
                .findByCashBoxIdAndStatusAndUserId(id, CashDrawerStatus.OPEN, userId)
                .orElseThrow(() -> new BaseException("No hay cajas abiertas para el cash box", List.of("")));

        if (existingDrawer.getStatus() == CashDrawerStatus.CLOSED) {
            throw new BaseException("La caja esta cerrada", List.of(""));
        }

        existingDrawer.setStatus(CashDrawerStatus.CLOSED);
        existingDrawer.setCloseDate(LocalDateTime.now());

        existingDrawer.getCashBox().setOpened(false);
        existingDrawer.getCashBox().setOpenedByUser(null);

        existingDrawer.setClosedByUser(existingDrawer.getOpenedByUser());
        existingDrawer.setClosedBalance(getActualBalance(existingDrawer));

        repository.save(existingDrawer);

        return  this.getCloseReport(existingDrawer.getId());
    }
    @Transactional
    public CashDrawer openDrawer(String cashBoxId, String userId, Double openingBalance, String detail) {

        var cashBox = cashBoxRepository.findById(cashBoxId).orElseThrow(
                () -> new BaseException("Cash box not found", List.of(""))
        );

        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );

        var userOpenDrawer = repository.findByStatusIsNotAndOpenedByUserId(CashDrawerStatus.CLOSED, userId);

        if (userOpenDrawer.isPresent()) {
            var drawer = userOpenDrawer.get();
            throw new BaseException(
                    "Ya tiene una caja abierta!",
                    List.of("El usuario " + user.getName() +
                            " ya tiene abierta la caja Nº " + drawer.getCashBox().getNumber())
            );
        }

        var openDrawerForBox = repository.findByCashBoxIdAndStatus(cashBoxId, CashDrawerStatus.OPEN);

        if (openDrawerForBox.isPresent()) {
            var drawer = openDrawerForBox.get();
            throw new BaseException(
                    "Caja ya abierta!",
                    List.of("La caja Nº " + cashBox.getNumber() +
                            " está abierta por " + drawer.getOpenedByUser().getName())
            );
        }

        cashBox.setOpened(true);
        cashBox.setOpenedByUser(user.getName());
        var newDrawer = new CashDrawer(
                UUID.randomUUID().toString(),
                CashDrawerStatus.OPEN,
                user,
                null,
                LocalDateTime.now(),
                null,
                openingBalance,
                null,
                detail,
                cashBox
        );

        return save(newDrawer);
    }


    public Double getActualBalance(CashDrawer cashDrawer) {
        Double totalPayments = 0.0;
        if (cashDrawer.getCashDrawerDetails() == null || cashDrawer.getCashDrawerDetails().isEmpty()) {
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

    public void lockCashDrawer(String id, String pin) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        if (cashDrawer.getStatus() == CashDrawerStatus.OPEN) {
            cashDrawer.setStatus(CashDrawerStatus.LOCKED);
            cashDrawer.setPin(pin);
            repository.save(cashDrawer);
        } else if (cashDrawer.getStatus() == CashDrawerStatus.LOCKED) {
            if (pin.equals(cashDrawer.getPin())) {
                cashDrawer.setStatus(CashDrawerStatus.OPEN);
                cashDrawer.setPin(null);
                repository.save(cashDrawer);
            } else {
                throw new BaseException("Pin incorrecto", List.of(""));
            }
        }
    }

    public ByteArrayOutputStream getCloseReport(String id) {
        var existingDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("La caja no existe", List.of(""))
        );

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(repository.generatePdfReport(existingDrawer));
            renderer.layout();
            renderer.createPDF(pdf);
            return pdf;
        } catch (Exception e) {
            throw new BaseException("Error generating invoice", List.of(e.getMessage()));
        }
    }

    public void forgetPin(String id) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        sendgrid.send(
                cashDrawer.getOpenedByUser().getEmail(),
                "Verificación de correo",
                "Este es tu token de recuperación de contraseña: "
        );

//                Email toEmail = new Email(
//                cashDrawer.getOpenedByUser().getEmail());
//
//        Content content = new Content("text/plain", cashDrawer.getPin());
//        Mail mail = new Mail(cashDrawer.getOpenedByUser().getEmail(), "Recuperacion de pin", toEmail, content);
//
//        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            throw new BaseException("Error sending email", List.of(ex.getMessage()));
//        }
    }
}
