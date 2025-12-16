package com.simplecash.projet_akoum_mohamad.application.dto;

import java.math.BigDecimal;

public class TransferCommand {
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    
    public TransferCommand() {
    }
    
    public TransferCommand(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }
    
    public Long getSourceAccountId() {
        return sourceAccountId;
    }
    
    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }
    
    public Long getTargetAccountId() {
        return targetAccountId;
    }
    
    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

