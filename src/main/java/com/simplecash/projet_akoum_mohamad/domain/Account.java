package com.simplecash.projet_akoum_mohamad.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "accounts")
public abstract class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    
    @Column(name = "opening_date", nullable = false)
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

