package com.simplecash.projet_akoum_mohamad.application.dto;

import java.math.BigDecimal;

public class DebitAccountCommand {
    private Long accountId;
    private BigDecimal amount;
    
    public DebitAccountCommand() {
    }
    
    public DebitAccountCommand(Long accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

