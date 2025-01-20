package com.foryourlife.payments.payment.seeders;

import com.foryourlife.payments.payment.domain.Payment;
import com.foryourlife.payments.payment.domain.PaymentRepository;
import com.foryourlife.payments.plan.domain.Plan;
import com.foryourlife.payments.plan.domain.PlanRepository;
import com.foryourlife.payments.tenant.domain.Tenant;
import com.foryourlife.payments.tenant.domain.TenantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;

@Configuration
public class PaymentSeeder {
    private final PlanRepository _planRepository;
    private final PaymentRepository _paymentRepository;
    private final TenantRepository _tenantRepository;

    public PaymentSeeder(PlanRepository _planRepository, PaymentRepository _paymentRepository, TenantRepository _tenantRepository) {
        this._planRepository = _planRepository;
        this._paymentRepository = _paymentRepository;
        this._tenantRepository = _tenantRepository;
    }

    @Bean
    CommandLineRunner initPayments() {
        return args -> {
            this._planRepository.save(Plan.create("70d786ab-74cd-4223-9a37-c59613ab35eb", "base64Image", "For", "Epic type", "Random voucher", "userid i guess", true, true, true, 247.44F));
            this._tenantRepository.save(Tenant.create("c2fe2a8b-607c-4be5-8e63-2dac88ed0a45", "Tenantdocument", "epicId", true));
            this._paymentRepository.save(Payment.create("e3ad46ac-d82d-48b2-b2f5-166353eb7b3e", "0986", "000239420", "0087493789", "0989956321", "lowkey@email.com", "Debit", "Pending", "787", 365.43F, Date.valueOf("2025-01-20"), this._planRepository.findById("70d786ab-74cd-4223-9a37-c59613ab35eb").get(), this._tenantRepository.findById("c2fe2a8b-607c-4be5-8e63-2dac88ed0a45").get()));
        };
    }
}
