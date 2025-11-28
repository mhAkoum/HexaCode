package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Client;
import com.simplecash.projet_akoum_mohamad.domain.ClientType;
import com.simplecash.projet_akoum_mohamad.domain.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.dto.AuditReportDTO;
import com.simplecash.projet_akoum_mohamad.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private AuditService auditService;
    
    private Client privateClient;
    private Client businessClient;
    private CurrentAccount privateCurrentAccount;
    private SavingsAccount privateSavingsAccount;
    private CurrentAccount businessCurrentAccount;
    
    @BeforeEach
    void setUp() {
        privateClient = new Client();
        privateClient.setId(1L);
        privateClient.setName("Private Client");
        privateClient.setClientType(ClientType.PRIVATE);
        
        privateCurrentAccount = new CurrentAccount();
        privateCurrentAccount.setId(1L);
        privateCurrentAccount.setAccountNumber("CA001");
        privateCurrentAccount.setBalance(new BigDecimal("1000.00"));
        privateCurrentAccount.setOpeningDate(LocalDate.now());
        privateClient.setCurrentAccount(privateCurrentAccount);
        
        privateSavingsAccount = new SavingsAccount();
        privateSavingsAccount.setId(2L);
        privateSavingsAccount.setAccountNumber("SA001");
        privateSavingsAccount.setBalance(new BigDecimal("500.00"));
        privateSavingsAccount.setOpeningDate(LocalDate.now());
        privateClient.setSavingsAccount(privateSavingsAccount);
        
        businessClient = new Client();
        businessClient.setId(2L);
        businessClient.setName("Business Client");
        businessClient.setClientType(ClientType.BUSINESS);
        
        businessCurrentAccount = new CurrentAccount();
        businessCurrentAccount.setId(3L);
        businessCurrentAccount.setAccountNumber("CA002");
        businessCurrentAccount.setBalance(new BigDecimal("-10000.00"));
        businessCurrentAccount.setOpeningDate(LocalDate.now());
        businessClient.setCurrentAccount(businessCurrentAccount);
    }
    
    @Test
    void testAuditAllAccounts_Success() {
        List<Client> clients = new ArrayList<>();
        clients.add(privateClient);
        clients.add(businessClient);
        
        when(clientRepository.findAll()).thenReturn(clients);
        
        AuditReportDTO report = auditService.auditAllAccounts();
        
        assertNotNull(report);
        assertEquals(3, report.getTotalAccountsAudited());
        assertEquals(2, report.getCreditAccounts().size());
        assertEquals(1, report.getDebitAccounts().size());
        assertEquals(0, report.getThresholdViolations().size());
        
        BigDecimal expectedCredits = new BigDecimal("1500.00");
        BigDecimal expectedDebits = new BigDecimal("-10000.00");
        
        assertTrue(report.getTotalCredits().compareTo(expectedCredits) == 0);
        assertTrue(report.getTotalDebits().compareTo(expectedDebits) == 0);
    }
    
    @Test
    void testAuditAllAccounts_ThresholdViolation_Private() {
        privateCurrentAccount.setBalance(new BigDecimal("-6000.00"));
        
        List<Client> clients = new ArrayList<>();
        clients.add(privateClient);
        
        when(clientRepository.findAll()).thenReturn(clients);
        
        AuditReportDTO report = auditService.auditAllAccounts();
        
        assertEquals(1, report.getThresholdViolations().size());
        assertTrue(report.getThresholdViolations().get(0).isViolatesThreshold());
        assertEquals(1, report.getAccountsWithViolations());
    }
    
    @Test
    void testAuditAllAccounts_ThresholdViolation_Business() {
        businessCurrentAccount.setBalance(new BigDecimal("-60000.00"));
        
        List<Client> clients = new ArrayList<>();
        clients.add(businessClient);
        
        when(clientRepository.findAll()).thenReturn(clients);
        
        AuditReportDTO report = auditService.auditAllAccounts();
        
        assertEquals(1, report.getThresholdViolations().size());
        assertTrue(report.getThresholdViolations().get(0).isViolatesThreshold());
    }
    
    @Test
    void testAuditAllAccounts_NoAccounts() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());
        
        AuditReportDTO report = auditService.auditAllAccounts();
        
        assertEquals(0, report.getTotalAccountsAudited());
        assertEquals(0, report.getCreditAccounts().size());
        assertEquals(0, report.getDebitAccounts().size());
    }
}

