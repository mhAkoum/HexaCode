package com.simplecash.projet_akoum_mohamad.web;

import com.simplecash.projet_akoum_mohamad.dto.AuditReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuditControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/audit";
    }
    
    @Test
    void testAuditAccounts_Success() {
        ResponseEntity<AuditReportDTO> response = restTemplate.getForEntity(
                baseUrl + "/accounts", AuditReportDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        AuditReportDTO report = response.getBody();
        assertNotNull(report.getCreditAccounts());
        assertNotNull(report.getDebitAccounts());
        assertNotNull(report.getThresholdViolations());
        assertNotNull(report.getTotalCredits());
        assertNotNull(report.getTotalDebits());
        assertTrue(report.getTotalAccountsAudited() >= 0);
        assertTrue(report.getAccountsWithViolations() >= 0);
    }
    
    @Test
    void testAuditAccounts_ResponseStructure() {
        ResponseEntity<AuditReportDTO> response = restTemplate.getForEntity(
                baseUrl + "/accounts", AuditReportDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuditReportDTO report = response.getBody();
        
        assertNotNull(report);
        assertNotNull(report.getCreditAccounts());
        assertNotNull(report.getDebitAccounts());
        assertNotNull(report.getThresholdViolations());
    }
}

