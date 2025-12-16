package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AccountPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AccountJpaRepository;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.ClientJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.AccountRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountPersistenceAdapter implements AccountRepositoryPort {
    
    private final AccountJpaRepository jpaRepository;
    private final ClientJpaRepository clientJpaRepository;
    private final AccountPersistenceMapper mapper;
    
    public AccountPersistenceAdapter(AccountJpaRepository jpaRepository,
                                    ClientJpaRepository clientJpaRepository,
                                    AccountPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.clientJpaRepository = clientJpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Account save(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        
        // Ensure client relationship is properly set
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            if (currentAccount.getClient() != null && currentAccount.getClient().getId() != null) {
                ClientEntity clientEntity = clientJpaRepository.findById(currentAccount.getClient().getId())
                        .orElse(null);
                if (clientEntity != null && entity instanceof CurrentAccountEntity) {
                    ((CurrentAccountEntity) entity).setClient(clientEntity);
                }
            }
        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if (savingsAccount.getClient() != null && savingsAccount.getClient().getId() != null) {
                ClientEntity clientEntity = clientJpaRepository.findById(savingsAccount.getClient().getId())
                        .orElse(null);
                if (clientEntity != null && entity instanceof SavingsAccountEntity) {
                    ((SavingsAccountEntity) entity).setClient(clientEntity);
                }
            }
        }
        
        AccountEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Account> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber)
                .map(mapper::toDomain);
    }
}

