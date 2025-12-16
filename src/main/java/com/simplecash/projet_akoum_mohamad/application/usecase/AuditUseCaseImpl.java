package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.AuditReport;
import com.simplecash.projet_akoum_mohamad.application.port.in.AuditUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.ClientRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;

import java.math.BigDecimal;
import java.util.List;

public class AuditUseCaseImpl implements AuditUseCase {
    
    private static final BigDecimal PRIVATE_THRESHOLD = new BigDecimal("-5000.00");
    private static final BigDecimal BUSINESS_THRESHOLD = new BigDecimal("-50000.00");
    
    private final ClientRepositoryPort clientRepository;
    
    public AuditUseCaseImpl(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Override
    public AuditReport auditAllAccounts() {
        AuditReport report = new AuditReport();
        
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
    
    private void processAccount(Account account, Client client, BigDecimal threshold, AuditReport report) {
        BigDecimal balance = account.getBalance();
        boolean violatesThreshold = balance.compareTo(threshold) < 0;
        
        AuditReport.AccountSummary summary = new AuditReport.AccountSummary(account, client, violatesThreshold);
        
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

