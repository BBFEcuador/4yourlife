package com.foryourlife.admin.sales.payments.cashDrawer.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.PaymentMethodSummary;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.*;
import java.util.stream.Stream;

@Service
public class JPACashDrawerRepository implements CashDrawerRepository {
    private final JPAImplCashDrawerRepository repository;
    private final JPACriteriaConverter<CashDrawer> criteriaConverter;
    private final CashDrawerDetailQueryService cashDrawerDetailQueryService;

    public JPACashDrawerRepository(JPAImplCashDrawerRepository repository, JPACriteriaConverter<CashDrawer> criteriaConverter, CashDrawerDetailQueryService cashDrawerDetailQueryService) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
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

        double totalIncome = Arrays.stream(paymentMethods)
                .mapToDouble(PaymentMethodSummary::getTotalAmount)
                .sum();

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
}