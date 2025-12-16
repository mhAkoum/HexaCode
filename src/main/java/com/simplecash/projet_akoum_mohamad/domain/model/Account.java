package com.simplecash.projet_akoum_mohamad.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Account {
    
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDate openingDate;
    
    public Account() {
    }
    
    public Account(String accountNumber, BigDecimal balance, LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.openingDate = openingDate;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public LocalDate getOpeningDate() {
        return openingDate;
    }
    
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
}

