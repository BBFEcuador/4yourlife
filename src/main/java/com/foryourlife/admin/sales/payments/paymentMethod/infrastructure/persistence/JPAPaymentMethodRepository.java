package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAPaymentMethodRepository implements PaymentMethodRepository {
    private final JPAImplPaymentMethodRepository repository;
    private final JPACriteriaConverter<PaymentMethod> criteriaConverter;

    public JPAPaymentMethodRepository(JPAImplPaymentMethodRepository repository, JPACriteriaConverter<PaymentMethod> criteriaConverter) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public void save(PaymentMethod paymentMethod) {
        repository.save(paymentMethod);
    }

    @Override
    public Optional<PaymentMethod> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void changeStatus(String id, Boolean isActive) {

        PaymentMethod paymentMethod = findById(id).orElseThrow(() -> new BaseException("Payment method not found", List.of("The payment method does not exist")));
        paymentMethod.setActive(isActive);
        repository.save(paymentMethod);

    }

    @Override
    public List<PaymentMethod> getAll(Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria));
    }

    @Override
    public Boolean exist(String id) {
        return repository.existsById(id);
    }
}
