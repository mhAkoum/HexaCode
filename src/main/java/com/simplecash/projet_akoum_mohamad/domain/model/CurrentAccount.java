package com.simplecash.projet_akoum_mohamad.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccount extends Account {
    
    private static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = new BigDecimal("1000.00");
    
    private Client client;
    private BigDecimal overdraftLimit;
    
    public CurrentAccount() {
        super();
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }
    
    public CurrentAccount(String accountNumber, BigDecimal balance, LocalDate openingDate) {
        super(accountNumber, balance, openingDate);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }
    
    public CurrentAccount(String accountNumber, BigDecimal balance, LocalDate openingDate, BigDecimal overdraftLimit) {
        super(accountNumber, balance, openingDate);
        this.overdraftLimit = overdraftLimit != null ? overdraftLimit : DEFAULT_OVERDRAFT_LIMIT;
    }
    
    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }
    
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
}

