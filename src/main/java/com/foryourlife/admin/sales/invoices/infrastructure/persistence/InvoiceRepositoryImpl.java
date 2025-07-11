package com.foryourlife.admin.sales.invoices.infrastructure.persistence;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final JPAInvoiceRepository jpaInvoiceRepository;
    private final JPACriteriaConverter<Invoice> criteriaConverter;

    public InvoiceRepositoryImpl(JPAInvoiceRepository jpaInvoiceRepository, JPACriteriaConverter<Invoice> criteriaConverter) {
        this.jpaInvoiceRepository = jpaInvoiceRepository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return jpaInvoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(String id) {
        return jpaInvoiceRepository.findById(id).orElseThrow(() -> new BaseException("Invoice not found", List.of("")));
    }

    @Override
    public Page<Invoice> findByUserId(String id, Pageable pageable) {
        return jpaInvoiceRepository.findAllByPayment_Participant_Id(id, pageable);
    }

    @Override
    public Page<Invoice> findAll(Pageable pageable, Criteria criteria) {
        return jpaInvoiceRepository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public Optional<Invoice> findLastInvoice() {
        return jpaInvoiceRepository.findTopByIsSentContificoIsTrueOrderByInvoiceDateDesc();
    }

    @Override
    public List<Invoice> findAllInvoiceByPaymentId(String paymentId) {
        return jpaInvoiceRepository.findAllByPayment_Id(paymentId);
    }

    @Override
    public Optional<Invoice> findInvoiceByPaymentId(String paymentId) {
        return jpaInvoiceRepository.findByPayment_Id(paymentId);
    }
}
