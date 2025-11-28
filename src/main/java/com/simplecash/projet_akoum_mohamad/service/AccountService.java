package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
    
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
    
    public Account credit(Long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be greater than 0");
        }
        
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        
        return accountRepository.save(account);
    }
    
    public Account credit(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be greater than 0");
        }
        
        Account account = getAccountByNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        
        return accountRepository.save(account);
    }
    
    public Account debit(Long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be greater than 0");
        }
        
        Account account = getAccountById(accountId);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            BigDecimal minimumBalance = currentAccount.getOverdraftLimit().negate();
            
            if (newBalance.compareTo(minimumBalance) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount,
                        currentAccount.getOverdraftLimit()
                );
            }
        } else if (account instanceof SavingsAccount) {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount
                );
            }
        } else {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount
                );
            }
        }
        
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    
    public Account debit(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be greater than 0");
        }
        
        Account account = getAccountByNumber(accountNumber);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            BigDecimal minimumBalance = currentAccount.getOverdraftLimit().negate();
            
            if (newBalance.compareTo(minimumBalance) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount,
                        currentAccount.getOverdraftLimit()
                );
            }
        } else if (account instanceof SavingsAccount) {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount
                );
            }
        } else {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        amount
                );
            }
        }
        
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
}

