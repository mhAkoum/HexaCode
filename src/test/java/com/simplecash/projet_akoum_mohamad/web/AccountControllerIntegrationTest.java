package com.simplecash.projet_akoum_mohamad.web;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreditRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.DebitRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.ErrorResponse;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AccountJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private AccountJpaRepository accountRepository;
    
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/accounts";
    }
    
    @Test
    void testGetAccountById_Success() {
        Long accountId = accountRepository.findAll().stream()
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/" + accountId, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"id\":" + accountId));
    }
    
    @Test
    void testGetAccountById_NotFound() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                baseUrl + "/99999", ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
    }
    
    @Test
    void testCreditAccount_Success() {
        Long accountId = accountRepository.findAll().stream()
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        CreditRequest request = new CreditRequest();
        request.setAmount(new BigDecimal("100.00"));
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/" + accountId + "/credit", request, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testCreditAccount_InvalidAmount() {
        Long accountId = accountRepository.findAll().stream()
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        CreditRequest request = new CreditRequest();
        request.setAmount(BigDecimal.ZERO);
        
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl + "/" + accountId + "/credit", request, ErrorResponse.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
    }
    
    @Test
    void testDebitAccount_Success() {
        Long accountId = accountRepository.findAll().stream()
                .filter(a -> a.getBalance().compareTo(new BigDecimal("100.00")) > 0)
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        DebitRequest request = new DebitRequest();
        request.setAmount(new BigDecimal("50.00"));
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/" + accountId + "/debit", request, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testDebitAccount_InsufficientFunds() {
        Long accountId = accountRepository.findAll().stream()
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        DebitRequest request = new DebitRequest();
        request.setAmount(new BigDecimal("1000000.00"));
        
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl + "/" + accountId + "/debit", request, ErrorResponse.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Insufficient funds"));
    }
}

