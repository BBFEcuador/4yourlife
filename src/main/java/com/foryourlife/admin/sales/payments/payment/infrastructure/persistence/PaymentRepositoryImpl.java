package com.foryourlife.admin.sales.payments.payment.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
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
import java.util.List;

@Service
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JPAPaymentRepository _jpaPaymentRepository;
    private final JPACriteriaConverter<Payment> converter;

    public PaymentRepositoryImpl(JPAPaymentRepository jpaPaymentRepository, JPACriteriaConverter<Payment> converter) {
        _jpaPaymentRepository = jpaPaymentRepository;
        this.converter = converter;
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
    public Page<Payment> findByParticipantId(String id, Pageable pageable) {
        return _jpaPaymentRepository.findByParticipantId(id, pageable);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return _jpaPaymentRepository.findAll(pageable);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable, Criteria criteria) {
        var jpaCriteria = converter.getJpaSpecifications(criteria);
        return _jpaPaymentRepository.findAll(jpaCriteria, pageable);
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

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("payment", payment);
        context.setVariable("date", payment.getCreated_at());
        context.setVariable("subtotal", BigDecimal.valueOf(payment.getTotal() / 1.15).setScale(2, RoundingMode.HALF_UP));
        context.setVariable("iva", BigDecimal.valueOf(payment.getTotal() - (payment.getTotal() / 1.15)).setScale(2, RoundingMode.HALF_UP));
        if (payment.getDiscount() != null) {
            context.setVariable("discount", payment.getDiscount().getDiscountValue());
        } else {
            context.setVariable("discount", 0.0);
        }

        return templateEngine.process("templates/Payment-pdf", context);
    }
}
