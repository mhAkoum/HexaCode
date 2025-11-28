package com.simplecash.projet_akoum_mohamad.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;
    
    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = false)
    private Account targetAccount;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "status", nullable = false)
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

