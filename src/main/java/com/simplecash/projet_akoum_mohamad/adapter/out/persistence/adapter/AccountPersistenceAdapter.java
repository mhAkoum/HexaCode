package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AccountEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AccountPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AccountJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.AccountRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class AccountPersistenceAdapter implements AccountRepositoryPort {
    
    private final AccountJpaRepository repository;
    
    public AccountPersistenceAdapter(AccountJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Account save(Account account) {
        AccountEntity entity = AccountPersistenceMapper.toEntity(account);
        
        // Handle client relationship if it's a CurrentAccount or SavingsAccount
        if (account instanceof com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount) {
            com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount currentAccount = 
                    (com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount) account;
            if (currentAccount.getClient() != null) {
                ((com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CurrentAccountEntity) entity)
                        .setClient(com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper
                                .toEntity(currentAccount.getClient()));
            }
        } else if (account instanceof com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount) {
            com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount savingsAccount = 
                    (com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount) account;
            if (savingsAccount.getClient() != null) {
                ((com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.SavingsAccountEntity) entity)
                        .setClient(com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper
                                .toEntity(savingsAccount.getClient()));
            }
        }
        
        AccountEntity saved = repository.save(entity);
        return AccountPersistenceMapper.toDomain(saved);
    }
    
    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id)
                .map(AccountPersistenceMapper::toDomain);
    }
    
    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .map(AccountPersistenceMapper::toDomain);
    }
}

