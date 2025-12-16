package com.simplecash.projet_akoum_mohamad.application.dto;

import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AuditReport {
    private List<AccountSummary> creditAccounts;
    private List<AccountSummary> debitAccounts;
    private List<AccountSummary> thresholdViolations;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private int totalAccountsAudited;
    private int accountsWithViolations;
    
    public AuditReport() {
        this.creditAccounts = new ArrayList<>();
        this.debitAccounts = new ArrayList<>();
        this.thresholdViolations = new ArrayList<>();
        this.totalCredits = BigDecimal.ZERO;
        this.totalDebits = BigDecimal.ZERO;
        this.totalAccountsAudited = 0;
        this.accountsWithViolations = 0;
    }
    
    public List<AccountSummary> getCreditAccounts() {
        return creditAccounts;
    }
    
    public void setCreditAccounts(List<AccountSummary> creditAccounts) {
        this.creditAccounts = creditAccounts;
    }
    
    public List<AccountSummary> getDebitAccounts() {
        return debitAccounts;
    }
    
    public void setDebitAccounts(List<AccountSummary> debitAccounts) {
        this.debitAccounts = debitAccounts;
    }
    
    public List<AccountSummary> getThresholdViolations() {
        return thresholdViolations;
    }
    
    public void setThresholdViolations(List<AccountSummary> thresholdViolations) {
        this.thresholdViolations = thresholdViolations;
    }
    
    public BigDecimal getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(BigDecimal totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    public BigDecimal getTotalDebits() {
        return totalDebits;
    }
    
    public void setTotalDebits(BigDecimal totalDebits) {
        this.totalDebits = totalDebits;
    }
    
    public int getTotalAccountsAudited() {
        return totalAccountsAudited;
    }
    
    public void setTotalAccountsAudited(int totalAccountsAudited) {
        this.totalAccountsAudited = totalAccountsAudited;
    }
    
    public int getAccountsWithViolations() {
        return accountsWithViolations;
    }
    
    public void setAccountsWithViolations(int accountsWithViolations) {
        this.accountsWithViolations = accountsWithViolations;
    }
    
    public static class AccountSummary {
        private Long accountId;
        private String accountNumber;
        private String accountType;
        private BigDecimal balance;
        private Long clientId;
        private String clientName;
        private String clientType;
        private boolean violatesThreshold;
        
        public AccountSummary() {
        }
        
        public AccountSummary(Account account, Client client, boolean violatesThreshold) {
            this.accountId = account.getId();
            this.accountNumber = account.getAccountNumber();
            this.accountType = account instanceof com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount 
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
}

