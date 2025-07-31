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
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
//            String fileName = "invoice_" + existingDrawer.getId() + ".pdf";
//            String filePath = Paths.get("").toAbsolutePath().toString() + File.separator + fileName;
//
//            try (FileOutputStream fos = new FileOutputStream(filePath)) {
//                fos.write(pdf.toByteArray());
//            }
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

    public void lockCashDrawer(String id, String pin){
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        if(cashDrawer.getStatus()==CashDrawerStatus.OPEN){
            cashDrawer.setStatus(CashDrawerStatus.LOCKED);
            cashDrawer.setPin(pin);
            repository.save(cashDrawer);
        }else if (cashDrawer.getStatus()==CashDrawerStatus.LOCKED){
            if(pin.equals(cashDrawer.getPin())){
                cashDrawer.setStatus(CashDrawerStatus.OPEN);
                cashDrawer.setPin(null);
                repository.save(cashDrawer);
            }else{
                throw new BaseException("Pin incorrecto", List.of(""));
            }
        }
    }

    public ByteArrayOutputStream getCloseReport(String id){
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
        }    }

    public void forgetPin(String id){
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );


        sendgrid.send(
                cashDrawer.getOpenedByUser().getEmail(),
                "Verificación de correo",
                "Este es tu token de recuperación de contraseña: ${builder.toString()}"
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
