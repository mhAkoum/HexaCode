package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.*;
import com.simplecash.projet_akoum_mohamad.application.port.in.AccountUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.AuditUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.ClientUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.TransferUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.*;
import com.simplecash.projet_akoum_mohamad.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    // Persistence Adapters (mappers are @Component, so they're auto-wired)
    @Bean
    public ClientRepositoryPort clientRepositoryPort(
            ClientJpaRepository jpaRepository,
            ClientPersistenceMapper mapper) {
        return new ClientPersistenceAdapter(jpaRepository, mapper);
    }
    
    @Bean
    public AccountRepositoryPort accountRepositoryPort(
            AccountJpaRepository jpaRepository,
            ClientJpaRepository clientJpaRepository,
            AccountPersistenceMapper mapper) {
        return new AccountPersistenceAdapter(jpaRepository, clientJpaRepository, mapper);
    }
    
    @Bean
    public AdvisorRepositoryPort advisorRepositoryPort(
            AdvisorJpaRepository jpaRepository,
            AdvisorPersistenceMapper mapper) {
        return new AdvisorPersistenceAdapter(jpaRepository, mapper);
    }
    
    @Bean
    public CardRepositoryPort cardRepositoryPort(
            CardJpaRepository jpaRepository,
            CardPersistenceMapper mapper) {
        return new CardPersistenceAdapter(jpaRepository, mapper);
    }
    
    @Bean
    public TransferRepositoryPort transferRepositoryPort(
            TransferJpaRepository jpaRepository,
            TransferPersistenceMapper mapper) {
        return new TransferPersistenceAdapter(jpaRepository, mapper);
    }
    
    // Use Cases
    @Bean
    public AccountUseCase accountUseCase(AccountRepositoryPort accountRepository) {
        return new AccountUseCaseImpl(accountRepository);
    }
    
    @Bean
    public ClientUseCase clientUseCase(
            ClientRepositoryPort clientRepository,
            AdvisorRepositoryPort advisorRepository,
            CardRepositoryPort cardRepository) {
        return new ClientUseCaseImpl(clientRepository, advisorRepository, cardRepository);
    }
    
    @Bean
    public TransferUseCase transferUseCase(
            AccountUseCase accountUseCase,
            TransferRepositoryPort transferRepository) {
        return new TransferUseCaseImpl(accountUseCase, transferRepository);
    }
    
    @Bean
    public AuditUseCase auditUseCase(ClientRepositoryPort clientRepository) {
        return new AuditUseCaseImpl(clientRepository);
    }
}

