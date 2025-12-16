package com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.AccountSummaryDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.AuditReportDTO;
import com.simplecash.projet_akoum_mohamad.application.dto.AuditReport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuditWebMapper {
    
    public AuditReportDTO toDTO(AuditReport report) {
        AuditReportDTO dto = new AuditReportDTO();
        
        dto.setCreditAccounts(report.getCreditAccounts().stream()
                .map(this::toAccountSummaryDTO)
                .collect(Collectors.toList()));
        
        dto.setDebitAccounts(report.getDebitAccounts().stream()
                .map(this::toAccountSummaryDTO)
                .collect(Collectors.toList()));
        
        dto.setThresholdViolations(report.getThresholdViolations().stream()
                .map(this::toAccountSummaryDTO)
                .collect(Collectors.toList()));
        
        dto.setTotalCredits(report.getTotalCredits());
        dto.setTotalDebits(report.getTotalDebits());
        dto.setTotalAccountsAudited(report.getTotalAccountsAudited());
        dto.setAccountsWithViolations(report.getAccountsWithViolations());
        
        return dto;
    }
    
    private AccountSummaryDTO toAccountSummaryDTO(AuditReport.AccountSummary summary) {
        // We need to reconstruct Account and Client from the summary
        // This is a simplified mapping - in a real scenario, you might want to fetch the full objects
        AccountSummaryDTO dto = new AccountSummaryDTO();
        dto.setAccountId(summary.getAccountId());
        dto.setAccountNumber(summary.getAccountNumber());
        dto.setAccountType(summary.getAccountType());
        dto.setBalance(summary.getBalance());
        dto.setClientId(summary.getClientId());
        dto.setClientName(summary.getClientName());
        dto.setClientType(summary.getClientType());
        dto.setViolatesThreshold(summary.isViolatesThreshold());
        return dto;
    }
}

