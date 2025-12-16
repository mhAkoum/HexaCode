package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AccountEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CurrentAccountEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.SavingsAccountEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount;

public class AccountPersistenceMapper {
    
    public static Account toDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        
        if (entity instanceof CurrentAccountEntity) {
            CurrentAccountEntity currentEntity = (CurrentAccountEntity) entity;
            CurrentAccount currentAccount = new CurrentAccount(
                    currentEntity.getAccountNumber(),
                    currentEntity.getBalance(),
                    currentEntity.getOpeningDate(),
                    currentEntity.getOverdraftLimit()
            );
            currentAccount.setId(currentEntity.getId());
            return currentAccount;
        } else if (entity instanceof SavingsAccountEntity) {
            SavingsAccountEntity savingsEntity = (SavingsAccountEntity) entity;
            SavingsAccount savingsAccount = new SavingsAccount(
                    savingsEntity.getAccountNumber(),
                    savingsEntity.getBalance(),
                    savingsEntity.getOpeningDate(),
                    savingsEntity.getInterestRate()
            );
            savingsAccount.setId(savingsEntity.getId());
            return savingsAccount;
        } else {
            // Fallback for base Account (shouldn't happen in practice)
            // If we get here, it means we have an AccountEntity that's not a subclass
            // This shouldn't happen, but we'll throw an exception
            throw new IllegalStateException("Cannot map AccountEntity to domain: unknown account type");
        }
    }
    
    public static AccountEntity toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        
        if (domain instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) domain;
            CurrentAccountEntity entity = new CurrentAccountEntity(
                    currentAccount.getAccountNumber(),
                    currentAccount.getBalance(),
                    currentAccount.getOpeningDate(),
                    currentAccount.getOverdraftLimit()
            );
            if (currentAccount.getId() != null) {
                entity.setId(currentAccount.getId());
            }
            return entity;
        } else if (domain instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) domain;
            SavingsAccountEntity entity = new SavingsAccountEntity(
                    savingsAccount.getAccountNumber(),
                    savingsAccount.getBalance(),
                    savingsAccount.getOpeningDate(),
                    savingsAccount.getInterestRate()
            );
            if (savingsAccount.getId() != null) {
                entity.setId(savingsAccount.getId());
            }
            return entity;
        } else {
            // Fallback (shouldn't happen)
            // If we get here, it means we have an Account that's not a subclass
            // This shouldn't happen, but we'll throw an exception
            throw new IllegalStateException("Cannot map Account to entity: unknown account type");
        }
    }
}

