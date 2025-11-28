package com.simplecash.projet_akoum_mohamad.dto;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.Client;

import java.math.BigDecimal;

public class AccountSummaryDTO {
    
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private Long clientId;
    private String clientName;
    private String clientType;
    private boolean violatesThreshold;
    
    public AccountSummaryDTO() {
    }
    
    public AccountSummaryDTO(Account account, Client client, boolean violatesThreshold) {
        this.accountId = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account instanceof com.simplecash.projet_akoum_mohamad.domain.CurrentAccount 
                ? "CURRENT" 
                : "SAVINGS";
        this.balance = account.getBalance();
        this.clientId = client.getId();
        this.clientName = client.getName();
        this.clientType = client.getClientType().name();
        this.violatesThreshold = violatesThreshold;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getClientType() {
        return clientType;
    }
    
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
    
    public boolean isViolatesThreshold() {
        return violatesThreshold;
    }
    
    public void setViolatesThreshold(boolean violatesThreshold) {
        this.violatesThreshold = violatesThreshold;
    }
}

