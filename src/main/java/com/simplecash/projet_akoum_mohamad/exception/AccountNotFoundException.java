package com.simplecash.projet_akoum_mohamad.exception;

public class AccountNotFoundException extends BusinessException {
    
    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with ID %d not found", accountId));
    }
    
    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with number %s not found", accountNumber));
    }
}

