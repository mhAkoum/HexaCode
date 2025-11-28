package com.simplecash.projet_akoum_mohamad.web;

import com.simplecash.projet_akoum_mohamad.domain.ClientType;
import com.simplecash.projet_akoum_mohamad.dto.CreateClientRequest;
import com.simplecash.projet_akoum_mohamad.dto.ErrorResponse;
import com.simplecash.projet_akoum_mohamad.repository.AdvisorRepository;
import com.simplecash.projet_akoum_mohamad.repository.ClientRepository;
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
class ClientControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private AdvisorRepository advisorRepository;
    
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/clients";
    }
    
    @Test
    void testGetAllClients() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetClientById_Success() {
        Long existingClientId = 1L;
        
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/" + existingClientId, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"id\":" + existingClientId));
    }
    
    @Test
    void testGetClientById_NotFound() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                baseUrl + "/99999", ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("not found"));
    }
    
    @Test
    void testCreateClient_SuccessOrConflict() {
        Long advisorId = advisorRepository.findAll().stream()
                .findFirst()
                .map(a -> a.getId())
                .orElse(1L);
        
        int clientCount = clientRepository.findByAdvisorId(advisorId).size();
        
        CreateClientRequest request = new CreateClientRequest();
        request.setName("Integration Test Client " + System.currentTimeMillis());
        request.setAddress("Test Address");
        request.setPhone("123-456-7890");
        request.setEmail("integration" + System.currentTimeMillis() + "@test.com");
        request.setClientType(ClientType.PRIVATE);
        request.setAdvisorId(advisorId);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, request, String.class);
        
        if (clientCount >= 10) {
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        } else {
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
        assertNotNull(response.getBody());
    }
    
    @Test
    void testCreateClient_AdvisorNotFound() {
        CreateClientRequest request = new CreateClientRequest();
        request.setName("Test Client");
        request.setAddress("Test Address");
        request.setPhone("123-456-7890");
        request.setEmail("test@test.com");
        request.setClientType(ClientType.PRIVATE);
        request.setAdvisorId(99999L);
        
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl, request, ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
    }
    
    @Test
    void testDeleteClient_NotFound() {
        restTemplate.delete(baseUrl + "/99999");
        
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                baseUrl + "/99999", ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
