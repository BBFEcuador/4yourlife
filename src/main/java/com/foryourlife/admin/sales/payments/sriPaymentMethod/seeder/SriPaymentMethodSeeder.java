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
                    createSriPaymentMethod("60c86914-4130-4e0e-95bf-246b869f1576" ,"SIN UTILIZACION DEL SISTEMA FINANCIERO", "01", "Efectivo"),
                    createSriPaymentMethod("9fa06a3a-caf8-419b-863d-30d3e0d9abf6" ,"CHEQUE PROPIO", "02", "Cheque Propio"),
                    createSriPaymentMethod("9aa8188f-bd67-4fa8-890a-3af8bf2fee11" ,"CHEQUE CERTIFICADO", "03", "Cheque Certificado"),
                    createSriPaymentMethod("5ebab7de-e8ff-409f-bd9a-bf18214b24d3" ,"CHEQUE DE GERENCIA", "04", "Cheque de Gerencia"),
                    createSriPaymentMethod("d7acfe44-0e67-400b-99dd-941876267995" ,"CHEQUE DEL EXTERIOR", "05", "Cheque del Exterior"),
                    createSriPaymentMethod("599908e6-6038-4db4-8c63-07936ad995d8" ,"DÉBITO DE CUENTA", "06", "Débito de Cuenta"),
                    createSriPaymentMethod("092df4f9-a52f-452f-b619-047d5536195a" ,"TRANSFERENCIA PROPIO BANCO", "07", "Transferencia Propio Banco"),
                    createSriPaymentMethod("5e4410b2-723e-4280-8c98-dab70602f600" ,"TRANSFERENCIA OTRO BANCO NACIONAL", "08", "Transferencia Otro Banco Nacional"),
                    createSriPaymentMethod("5199fc24-5198-423b-ade4-650db3943a09" ,"TRANSFERENCIA BANCO EXTERIOR", "09", "Transferencia Banco Exterior"),
                    createSriPaymentMethod("3554aa12-f6ca-467b-b9dd-3511959c37d2" ,"TARJETA DE CRÉDITO NACIONAL", "10", "Tarjeta de Crédito Nacional"),
                    createSriPaymentMethod("c26df24a-4d24-4c1c-9d8a-edee7412fa8e" ,"TARJETA DE CRÉDITO INTERNACIONAL", "11", "Tarjeta de Crédito Internacional"),
                    createSriPaymentMethod("c165c42e-9916-4959-80d4-68d8aee45d40" ,"GIRO", "12", "Giro"),
                    createSriPaymentMethod("f385e25e-2122-4456-982d-38b6e31f58be" ,"DEPOSITO EN CUENTA (CORRIENTE/AHORROS)", "13", "Depósito en Cuenta"),
                    createSriPaymentMethod("0b6bd9b6-3e68-4460-94d9-2e33ed8c3d7e" ,"ENDOSO DE INVERSIÓN", "14", "Endoso de Inversión"),
                    createSriPaymentMethod("43af2067-ecc1-43a3-8b1e-7d141576e234" ,"COMPENSACIÓN DE DEUDAS", "15", "Compensación de deudas"),
                    createSriPaymentMethod("2fe27465-473f-4746-8330-084ca1a44ab1" ,"TARJETA DE DÉBITO", "16", "Tarjeta de débito"),
                    createSriPaymentMethod("4f97cccd-f1e0-4ec2-b9ec-a9afaac6d8c8" ,"DINERO ELECTRÓNICO", "17", "Dinero electrónico"),
                    createSriPaymentMethod("08eb2234-9df1-4b89-9c15-75511f28518c" ,"TARJETA PREPAGO", "18", "Tarjeta prepago"),
                    createSriPaymentMethod("3a18cf7c-cca4-4c0e-8151-efd616174989" ,"TARJETA DE CRÉDITO", "19", "Tarjeta de crédito"),
                    createSriPaymentMethod("cfa6ab0b-2372-4f5c-981f-60058625d209" ,"OTROS CON UTILIZACION DEL SISTEMA FINANCIERO", "20", "Transferencia"),
                    createSriPaymentMethod("36ad34f1-1c49-4a85-af08-cbc0da98764c" ,"OTROS CON UTILIZACION DEL SISTEMA FINANCIERO", "20", "Cheque"),
                    createSriPaymentMethod("36f1fcb5-c127-4390-8386-dc174ced8d61" ,"ENDOSO DE TÍTULOS", "21", "Endoso de títulos")
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
                code,
                name != null ? name : method
        );
    }
}
