package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.*;
import com.simplecash.projet_akoum_mohamad.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {
    
    @Autowired
    private ClientPersistenceMapper clientMapper;
    
    public Account toDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Account account;
        if (entity instanceof CurrentAccountEntity) {
            CurrentAccountEntity currentEntity = (CurrentAccountEntity) entity;
            CurrentAccount currentAccount = new CurrentAccount(
                    entity.getAccountNumber(),
                    entity.getBalance(),
                    entity.getOpeningDate(),
                    currentEntity.getOverdraftLimit()
            );
            currentAccount.setId(entity.getId());
            if (currentEntity.getClient() != null) {
                currentAccount.setClient(clientMapper.toDomain(currentEntity.getClient()));
            }
            account = currentAccount;
        } else if (entity instanceof SavingsAccountEntity) {
            SavingsAccountEntity savingsEntity = (SavingsAccountEntity) entity;
            SavingsAccount savingsAccount = new SavingsAccount(
                    entity.getAccountNumber(),
                    entity.getBalance(),
                    entity.getOpeningDate(),
                    savingsEntity.getInterestRate()
            );
            savingsAccount.setId(entity.getId());
            if (savingsEntity.getClient() != null) {
                savingsAccount.setClient(clientMapper.toDomain(savingsEntity.getClient()));
            }
            account = savingsAccount;
        } else {
            throw new IllegalArgumentException("Unknown account entity type: " + entity.getClass());
        }
        
        return account;
    }
    
    public AccountEntity toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        
        AccountEntity entity;
        if (domain instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) domain;
            CurrentAccountEntity currentEntity = new CurrentAccountEntity(
                    domain.getAccountNumber(),
                    domain.getBalance(),
                    domain.getOpeningDate(),
                    currentAccount.getOverdraftLimit()
            );
            currentEntity.setId(domain.getId());
            if (currentAccount.getClient() != null && currentAccount.getClient().getId() != null) {
                ClientEntity clientEntity = new ClientEntity();
                clientEntity.setId(currentAccount.getClient().getId());
                currentEntity.setClient(clientEntity);
            }
            entity = currentEntity;
        } else if (domain instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) domain;
            SavingsAccountEntity savingsEntity = new SavingsAccountEntity(
                    domain.getAccountNumber(),
                    domain.getBalance(),
                    domain.getOpeningDate(),
                    savingsAccount.getInterestRate()
            );
            savingsEntity.setId(domain.getId());
            if (savingsAccount.getClient() != null && savingsAccount.getClient().getId() != null) {
                ClientEntity clientEntity = new ClientEntity();
                clientEntity.setId(savingsAccount.getClient().getId());
                savingsEntity.setClient(clientEntity);
            }
            entity = savingsEntity;
        } else {
            throw new IllegalArgumentException("Unknown account domain type: " + domain.getClass());
        }
        
        return entity;
    }
}

