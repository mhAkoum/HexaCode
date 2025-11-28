package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.Client;
import com.simplecash.projet_akoum_mohamad.domain.ClientType;
import com.simplecash.projet_akoum_mohamad.dto.AccountSummaryDTO;
import com.simplecash.projet_akoum_mohamad.dto.AuditReportDTO;
import com.simplecash.projet_akoum_mohamad.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuditService {
    
    private static final BigDecimal PRIVATE_THRESHOLD = new BigDecimal("-5000.00");
    private static final BigDecimal BUSINESS_THRESHOLD = new BigDecimal("-50000.00");
    
    private final ClientRepository clientRepository;
    
    @Autowired
    public AuditService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public AuditReportDTO auditAllAccounts() {
        AuditReportDTO report = new AuditReportDTO();
        
        List<Client> allClients = clientRepository.findAll();
        
        for (Client client : allClients) {
            BigDecimal threshold = client.getClientType() == ClientType.PRIVATE 
                    ? PRIVATE_THRESHOLD 
                    : BUSINESS_THRESHOLD;
            
            if (client.getCurrentAccount() != null) {
                processAccount(client.getCurrentAccount(), client, threshold, report);
            }
            
            if (client.getSavingsAccount() != null) {
                processAccount(client.getSavingsAccount(), client, threshold, report);
            }
        }
        
        return report;
    }
    
    private void processAccount(Account account, Client client, BigDecimal threshold, AuditReportDTO report) {
        BigDecimal balance = account.getBalance();
        boolean violatesThreshold = balance.compareTo(threshold) < 0;
        
        AccountSummaryDTO summary = new AccountSummaryDTO(account, client, violatesThreshold);
        
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            report.getCreditAccounts().add(summary);
            report.setTotalCredits(report.getTotalCredits().add(balance));
        } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
            report.getDebitAccounts().add(summary);
            report.setTotalDebits(report.getTotalDebits().add(balance));
        }
        
        if (violatesThreshold) {
            report.getThresholdViolations().add(summary);
            report.setAccountsWithViolations(report.getAccountsWithViolations() + 1);
        }
        
        report.setTotalAccountsAudited(report.getTotalAccountsAudited() + 1);
    }
}

