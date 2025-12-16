package com.simplecash.projet_akoum_mohamad.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transfer {
    
    private Long id;
    private Account sourceAccount;
    private Account targetAccount;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;
    
    public Transfer() {
    }
    
    public Transfer(Account sourceAccount, Account targetAccount, BigDecimal amount, LocalDateTime timestamp, String status) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Account getSourceAccount() {
        return sourceAccount;
    }
    
    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    
    public Account getTargetAccount() {
        return targetAccount;
    }
    
    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

