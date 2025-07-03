package com.foryourlife.admin.sales.payments.sriPaymentMethod.seeder;

import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethod;
import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Configuration
public class SriPaymentMethodSeeder {
    private final SriPaymentMethodRepository sriPaymentMethodRepository;

    public SriPaymentMethodSeeder(SriPaymentMethodRepository sriPaymentMethodRepository) {
        this.sriPaymentMethodRepository = sriPaymentMethodRepository;
    }

    @Bean
    CommandLineRunner initSriPaymentMethods() {
        return args ->{
            List<SriPaymentMethod> paymentMethods = Arrays.asList(
                    createSriPaymentMethod("60c86914-4130-4e0e-95bf-246b869f1576" ,"Efectivo", "EF", "Efectivo"),
                    createSriPaymentMethod("9fa06a3a-caf8-419b-863d-30d3e0d9abf6" ,"Cheque", "CQ", "Cheque Propio"),
                    createSriPaymentMethod("9aa8188f-bd67-4fa8-890a-3af8bf2fee11" ,"Tarjeta de Crédito", "TC", "Tarjeta de Crédito"),
                    createSriPaymentMethod("5ebab7de-e8ff-409f-bd9a-bf18214b24d3" ,"Transferencia", "TRA", "Transferencia")
            );

            for (SriPaymentMethod paymentMethod : paymentMethods) {
                sriPaymentMethodRepository.save(paymentMethod);
            }
        };


    }

    private SriPaymentMethod createSriPaymentMethod(String id, String method, String code, String name) {
        return new SriPaymentMethod(
                id,
                method,
                name != null ? name : method,
                code
        );
    }
}
