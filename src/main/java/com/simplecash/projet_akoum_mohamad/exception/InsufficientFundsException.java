package com.simplecash.projet_akoum_mohamad.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends BusinessException {
    
    public InsufficientFundsException(String accountNumber, BigDecimal balance, BigDecimal requestedAmount, BigDecimal overdraftLimit) {
        super(String.format(
            "Insufficient funds in account %s. Balance: %s, Requested: %s, Overdraft limit: %s, Available: %s",
            accountNumber,
            balance,
            requestedAmount,
            overdraftLimit,
            balance.add(overdraftLimit)
        ));
    }
    
    public InsufficientFundsException(String accountNumber, BigDecimal balance, BigDecimal requestedAmount) {
        super(String.format(
            "Insufficient funds in account %s. Balance: %s, Requested: %s",
            accountNumber,
            balance,
            requestedAmount
        ));
    }
}

