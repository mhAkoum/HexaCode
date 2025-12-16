package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.port.in.AccountUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.AccountRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;

import java.math.BigDecimal;

public class AccountUseCaseImpl implements AccountUseCase {
    
    private final AccountRepositoryPort accountRepository;
    
    public AccountUseCaseImpl(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
    
    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
    
    @Override
    public Account creditAccount(CreditAccountCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be greater than 0");
        }
        
        Account account = getAccountById(command.getAccountId());
        account.setBalance(account.getBalance().add(command.getAmount()));
        
        return accountRepository.save(account);
    }
    
    @Override
    public Account debitAccount(DebitAccountCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be greater than 0");
        }
        
        Account account = getAccountById(command.getAccountId());
        BigDecimal newBalance = account.getBalance().subtract(command.getAmount());
        
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            BigDecimal minimumBalance = currentAccount.getOverdraftLimit().negate();
            
            if (newBalance.compareTo(minimumBalance) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        command.getAmount(),
                        currentAccount.getOverdraftLimit()
                );
            }
        } else if (account instanceof SavingsAccount) {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        command.getAmount()
                );
            }
        } else {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(
                        account.getAccountNumber(),
                        account.getBalance(),
                        command.getAmount()
                );
            }
        }
        
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
}

