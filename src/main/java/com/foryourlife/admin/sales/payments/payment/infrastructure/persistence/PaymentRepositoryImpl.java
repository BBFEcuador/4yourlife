package com.foryourlife.admin.sales.payments.payment.infrastructure.persistence;

import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JPAPaymentRepository _jpaPaymentRepository;
    private final JPACriteriaConverter<Payment> converter;
    private final ConfigContificoQueryService configContificoQueryService;

    public PaymentRepositoryImpl(JPAPaymentRepository _jpaPaymentRepository, JPACriteriaConverter<Payment> converter, ConfigContificoQueryService configContificoQueryService) {
        this._jpaPaymentRepository = _jpaPaymentRepository;
        this.converter = converter;
        this.configContificoQueryService = configContificoQueryService;
    }

    @Override
    public Payment save(Payment payment) {
        return _jpaPaymentRepository.save(payment);
    }

    @Override
    public Payment findById(String id) {
        return _jpaPaymentRepository.findById(id).orElseThrow(() -> new BaseException("Payment not found", List.of("")));
    }

    @Override
    public Page<Payment> findByAllParticipantId(String id, Pageable pageable) {
        return _jpaPaymentRepository.findByParticipantId(id, pageable);
    }

    @Override
    public List<Payment> findByAllParticipantId(String id) {
        return _jpaPaymentRepository.findByParticipantId(id);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return _jpaPaymentRepository.findAll(pageable);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable, Criteria criteria) {
        return _jpaPaymentRepository.findAll(converter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public boolean existsByParticipantIdAndStatus(String participantId, PaymentStatus status) {
        return _jpaPaymentRepository.existsByParticipant_IdAndStatus(participantId, status);
    }

    @Override
    public String generatePdf(Payment payment) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        var config = configContificoQueryService.findConfigContificoByCampusId(payment.getCampus().getId());
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("payment", payment);
        context.setVariable("date", payment.getCreated_at());
        context.setVariable("subtotal", payment.getTotal().divide(BigDecimal.valueOf(1.15), 2, RoundingMode.HALF_UP));
        context.setVariable("iva", payment.getTotal().subtract(payment.getTotal().divide(BigDecimal.valueOf(1.15), 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
        context.setVariable("config", config);
        context.setVariable(
                "discount",
                payment.getDiscount() != null && payment.getDiscount().getDiscountValue() != null
                        ? payment.getDiscount().getDiscountValue()
                        : 0
        );
        context.setVariable("paymentList", payment.getPaymentshistory());
        BigDecimal total = payment.getPaymentshistory().stream()
                .map(p -> BigDecimal.valueOf(p.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        context.setVariable("totalPaid", total.setScale(2, RoundingMode.HALF_UP));
        return templateEngine.process("templates/Payment-pdf", context);
    }

    @Override
    public List<Payment> findAllByParticipantIn(Collection<Participant> participantIds) {
        return _jpaPaymentRepository.findAllByParticipantIn(participantIds);
    }

    @Override
    public List<Payment> findAllBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return _jpaPaymentRepository.findAllByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public Optional<Payment> findFirstByParticipantIdAndStatusOrderByCreatedDateAsc(String participantId, PaymentStatus status) {
        return _jpaPaymentRepository.findFirstByParticipant_IdAndStatusOrderByCreatedDateAsc(participantId, status);
    }

    @Override
    public List<Payment> findAllByTrainingId(String trainingId) {
        return _jpaPaymentRepository.findAllByTraining_Id(trainingId);
    }
}
