package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.application.QueryPaymentService;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class JPACashDrawerRepository implements CashDrawerRepository {
    private final JPAImplCashDrawerRepository repository;
    private final CashDrawerDetailQueryService cashDrawerDetailQueryService;

    public JPACashDrawerRepository(JPAImplCashDrawerRepository repository, CashDrawerDetailQueryService cashDrawerDetailQueryService) {
        this.repository = repository;
        this.cashDrawerDetailQueryService = cashDrawerDetailQueryService;
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
    public List<CashDrawer> getAll() {
        return this.repository.findAll();
    }

    @Override
    public List<CashDrawer> getByUserIdAndStatusOpen(String userid) {
        return this.repository.findAllByStatusAndOpenedByUser_Id(CashDrawerStatus.OPEN, userid);
    }

    @Override
    public Optional<CashDrawer> getByIsOpenAndByUserId(String userId) {
        return this.repository.findByStatusAndOpenedByUser_Id(CashDrawerStatus.OPEN, userId);
    }

    @Override
    public Optional<CashDrawer> findByCashBoxIdAndStatus(String id, CashDrawerStatus status) {
        return repository.findByStatusIsAndCashBox_Id(status, id);
    }

    @Override
    public List<CashDrawer> getByCashBoxId(String id) {
        return this.repository.findAllByCashBox_Id(id);
    }

    @Override
    public String generatePdfReport(CashDrawer cashDrawer) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        var details = cashDrawerDetailQueryService.getByCashDrawerId(cashDrawer.getId());

        Map<String, PaymentMethodSummary> paymentMethodMap = getStringPaymentMethodSummaryMap(details);

        PaymentMethodSummary[] paymentMethods = paymentMethodMap.values().toArray(new PaymentMethodSummary[0]);

        System.out.println("Number of payment methods found: " + paymentMethods.length);
        for (PaymentMethodSummary summary : paymentMethods) {
            System.out.println("Payment Method: " + summary.getPaymentMethod().getType() +
                    ", Total Amount: " + summary.getTotalAmount() +
                    ", Transaction Count: " + summary.getTransactionCount());
        }

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();

        context.setVariable("cashDrawer", cashDrawer);
        context.setVariable("details", details);
        context.setVariable("paymentMethods", paymentMethods);
        return templateEngine.process("templates/Report-cash-drawer-pdf", context);
    }

    @NotNull
    private static Map<String, PaymentMethodSummary> getStringPaymentMethodSummaryMap(List<CashDrawerDetail> details) {
        Map<String, PaymentMethodSummary> paymentMethodMap = new HashMap<>();

        for (var detail : details) {
            for (var paymentHistory : detail.getPayment().getPaymentshistory()) {
                String paymentMethodId = paymentHistory.getPaymentMethod().getId();
                PaymentMethodSummary summary = paymentMethodMap.get(paymentMethodId);

                if (summary == null) {
                    summary = new PaymentMethodSummary(paymentHistory.getPaymentMethod());
                    paymentMethodMap.put(paymentMethodId, summary);
                }

                summary.addPayment(paymentHistory.getAmount());
            }
        }
        return paymentMethodMap;
    }
}
