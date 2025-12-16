package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter.*;
import com.simplecash.projet_akoum_mohamad.application.port.in.AccountUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.AuditUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.ClientUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.TransferUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.*;
import com.simplecash.projet_akoum_mohamad.application.usecase.AccountUseCaseImpl;
import com.simplecash.projet_akoum_mohamad.application.usecase.AuditUseCaseImpl;
import com.simplecash.projet_akoum_mohamad.application.usecase.ClientUseCaseImpl;
import com.simplecash.projet_akoum_mohamad.application.usecase.TransferUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    // Repository Ports (implemented by persistence adapters)
    @Bean
    public ClientRepositoryPort clientRepositoryPort(ClientPersistenceAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public AccountRepositoryPort accountRepositoryPort(AccountPersistenceAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public AdvisorRepositoryPort advisorRepositoryPort(AdvisorPersistenceAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public CardRepositoryPort cardRepositoryPort(CardPersistenceAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public TransferRepositoryPort transferRepositoryPort(TransferPersistenceAdapter adapter) {
        return adapter;
    }
    
    // Use Cases
    @Bean
    public AccountUseCase accountUseCase(AccountRepositoryPort accountRepositoryPort) {
        return new AccountUseCaseImpl(accountRepositoryPort);
    }
    
    @Bean
    public ClientUseCase clientUseCase(
            ClientRepositoryPort clientRepositoryPort,
            AdvisorRepositoryPort advisorRepositoryPort,
            CardRepositoryPort cardRepositoryPort) {
        return new ClientUseCaseImpl(clientRepositoryPort, advisorRepositoryPort, cardRepositoryPort);
    }
    
    @Bean
    public TransferUseCase transferUseCase(
            AccountUseCase accountUseCase,
            TransferRepositoryPort transferRepositoryPort) {
        return new TransferUseCaseImpl(accountUseCase, transferRepositoryPort);
    }
    
    @Bean
    public AuditUseCase auditUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new AuditUseCaseImpl(clientRepositoryPort);
    }
}

