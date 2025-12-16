package com.simplecash.projet_akoum_mohamad.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsAccount extends Account {
    
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("3.00");
    
    private Client client;
    private BigDecimal interestRate;
    
    public SavingsAccount() {
        super();
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    public SavingsAccount(String accountNumber, BigDecimal balance, LocalDate openingDate) {
        super(accountNumber, balance, openingDate);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    public SavingsAccount(String accountNumber, BigDecimal balance, LocalDate openingDate, BigDecimal interestRate) {
        super(accountNumber, balance, openingDate);
        this.interestRate = interestRate != null ? interestRate : DEFAULT_INTEREST_RATE;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
}

