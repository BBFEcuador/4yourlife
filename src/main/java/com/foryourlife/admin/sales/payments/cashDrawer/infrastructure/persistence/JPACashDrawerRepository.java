package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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
    public List<CashDrawer> getByUserIdAndStatusOpenOrLock(String userid) {
        return this.repository.findAllByStatusIsNotAndOpenedByUser_Id(CashDrawerStatus.CLOSED, userid);
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

        double totalIncome = Arrays.stream(paymentMethods)
                .mapToDouble(PaymentMethodSummary::getTotalAmount)
                .sum();

        System.out.println("Number of payment methods found: " + paymentMethods.length);
        for (PaymentMethodSummary summary : paymentMethods) {
            System.out.println("Payment Method: " + summary.getPaymentMethod().getType() +
                    ", Total Amount: " + summary.getTotalAmount() +
                    ", Transaction Count: " + summary.getTransactionCount());
        }
        List<Map<String, Object>> simplifiedDetails = details.stream()
                .map(detail -> {
                    Map<String, Object> map = new HashMap<>();
                    PaymentHistory paymentHistory = findPaymentHistory(detail);
                    map.put("date", paymentHistory != null ? paymentHistory.getDate() : detail.getPayment().getCreated_at());
                    map.put("productName", detail.getPaymentHistoryId() != null && !detail.getPayment().getProducts().isEmpty()
                            ? detail.getPayment().getProducts().getFirst().getName() + " - ABONO" : detail.getPayment().getProducts().getFirst().getName() + " - COMPROMISO DE PAGO");
                    map.put("paymentMethod", paymentHistory != null && paymentHistory.getPaymentMethod() != null
                            ? paymentHistory.getPaymentMethod().getType() : "");
                    map.put("amount", paymentHistory != null ? paymentHistory.getAmount() : 0.0);
                    return map;
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
}