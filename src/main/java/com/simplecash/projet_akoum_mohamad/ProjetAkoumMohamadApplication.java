package com.simplecash.projet_akoum_mohamad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.simplecash.projet_akoum_mohamad",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.simplecash\\.projet_akoum_mohamad\\.web\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.simplecash\\.projet_akoum_mohamad\\.service\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.simplecash\\.projet_akoum_mohamad\\.exception\\.GlobalExceptionHandler")
    }
)
public class ProjetAkoumMohamadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetAkoumMohamadApplication.class, args);
    }

}
