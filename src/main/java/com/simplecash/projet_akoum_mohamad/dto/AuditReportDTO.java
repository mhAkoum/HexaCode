package com.simplecash.projet_akoum_mohamad.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AuditReportDTO {
    
    private List<AccountSummaryDTO> creditAccounts;
    private List<AccountSummaryDTO> debitAccounts;
    private List<AccountSummaryDTO> thresholdViolations;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private int totalAccountsAudited;
    private int accountsWithViolations;
    
    public AuditReportDTO() {
        this.creditAccounts = new ArrayList<>();
        this.debitAccounts = new ArrayList<>();
        this.thresholdViolations = new ArrayList<>();
        this.totalCredits = BigDecimal.ZERO;
        this.totalDebits = BigDecimal.ZERO;
        this.totalAccountsAudited = 0;
        this.accountsWithViolations = 0;
    }
    
    public List<AccountSummaryDTO> getCreditAccounts() {
        return creditAccounts;
    }
    
    public void setCreditAccounts(List<AccountSummaryDTO> creditAccounts) {
        this.creditAccounts = creditAccounts;
    }
    
    public List<AccountSummaryDTO> getDebitAccounts() {
        return debitAccounts;
    }
    
    public void setDebitAccounts(List<AccountSummaryDTO> debitAccounts) {
        this.debitAccounts = debitAccounts;
    }
    
    public List<AccountSummaryDTO> getThresholdViolations() {
        return thresholdViolations;
    }
    
    public void setThresholdViolations(List<AccountSummaryDTO> thresholdViolations) {
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
}

