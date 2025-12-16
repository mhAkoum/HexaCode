package com.simplecash.projet_akoum_mohamad;

import com.simplecash.projet_akoum_mohamad.web.ClientController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.simplecash.projet_akoum_mohamad",
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                com.simplecash.projet_akoum_mohamad.web.ClientController.class,
                com.simplecash.projet_akoum_mohamad.web.AccountController.class,
                com.simplecash.projet_akoum_mohamad.web.AuditController.class,
                com.simplecash.projet_akoum_mohamad.service.ClientService.class,
                com.simplecash.projet_akoum_mohamad.service.AccountService.class,
                com.simplecash.projet_akoum_mohamad.service.TransferService.class,
                com.simplecash.projet_akoum_mohamad.service.AuditService.class,
                com.simplecash.projet_akoum_mohamad.service.AdvisorService.class,
                com.simplecash.projet_akoum_mohamad.exception.GlobalExceptionHandler.class,
                com.simplecash.projet_akoum_mohamad.config.AccountServiceTester.class,
                com.simplecash.projet_akoum_mohamad.config.TransferServiceTester.class,
                com.simplecash.projet_akoum_mohamad.config.ServiceTester.class,
                com.simplecash.projet_akoum_mohamad.config.ComprehensiveTester.class
            }
        )
    }
)
public class ProjetAkoumMohamadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetAkoumMohamadApplication.class, args);
    }

}
