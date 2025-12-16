package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings_accounts")
@PrimaryKeyJoinColumn(name = "account_id")
public class SavingsAccountEntity extends AccountEntity {
    
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("3.00");
    
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private ClientEntity client;
    
    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;
    
    public SavingsAccountEntity() {
        super();
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    public SavingsAccountEntity(String accountNumber, BigDecimal balance, LocalDate openingDate) {
        super(accountNumber, balance, openingDate);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    public SavingsAccountEntity(String accountNumber, BigDecimal balance, LocalDate openingDate, BigDecimal interestRate) {
        super(accountNumber, balance, openingDate);
        this.interestRate = interestRate != null ? interestRate : DEFAULT_INTEREST_RATE;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public ClientEntity getClient() {
        return client;
    }
    
    public void setClient(ClientEntity client) {
        this.client = client;
    }
}

