# Testing Guide for Hexagonal Architecture

This guide explains how to test the refactored hexagonal architecture codebase.

## Testing Strategy

The hexagonal architecture enables three types of tests:

1. **Unit Tests for Use Cases** - Test business logic with mocked ports
2. **Integration Tests for Adapters** - Test adapter implementations
3. **End-to-End Tests** - Test the full application through REST endpoints

## 1. Unit Tests for Use Cases

Test use cases in isolation by mocking the port.out interfaces.

### Example: AccountUseCaseTest

```java
package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.port.out.AccountRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {
    
    @Mock
    private AccountRepositoryPort accountRepositoryPort;
    
    @InjectMocks
    private AccountUseCaseImpl accountUseCase;
    
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;
    
    @BeforeEach
    void setUp() {
        currentAccount = new CurrentAccount("CA001", 
            new BigDecimal("1000.00"), 
            LocalDate.now(), 
            new BigDecimal("1000.00"));
        currentAccount.setId(1L);
        
        savingsAccount = new SavingsAccount("SA001", 
            new BigDecimal("500.00"), 
            LocalDate.now(), 
            new BigDecimal("3.00"));
        savingsAccount.setId(2L);
    }
    
    @Test
    void testCreditAccount_Success() {
        when(accountRepositoryPort.findById(1L))
            .thenReturn(Optional.of(currentAccount));
        when(accountRepositoryPort.save(any(CurrentAccount.class)))
            .thenReturn(currentAccount);
        
        CreditAccountCommand command = new CreditAccountCommand(1L, new BigDecimal("200.00"));
        accountUseCase.creditAccount(command);
        
        assertEquals(new BigDecimal("1200.00"), currentAccount.getBalance());
        verify(accountRepositoryPort, times(1)).save(currentAccount);
    }
    
    @Test
    void testDebitAccount_CurrentAccount_WithinOverdraft() {
        currentAccount.setBalance(new BigDecimal("500.00"));
        when(accountRepositoryPort.findById(1L))
            .thenReturn(Optional.of(currentAccount));
        when(accountRepositoryPort.save(any(CurrentAccount.class)))
            .thenReturn(currentAccount);
        
        DebitAccountCommand command = new DebitAccountCommand(1L, new BigDecimal("1200.00"));
        accountUseCase.debitAccount(command);
        
        assertEquals(new BigDecimal("-700.00"), currentAccount.getBalance());
        verify(accountRepositoryPort, times(1)).save(currentAccount);
    }
    
    @Test
    void testDebitAccount_SavingsAccount_InsufficientFunds() {
        when(accountRepositoryPort.findById(2L))
            .thenReturn(Optional.of(savingsAccount));
        
        DebitAccountCommand command = new DebitAccountCommand(2L, new BigDecimal("1000.00"));
        
        assertThrows(InsufficientFundsException.class, () -> {
            accountUseCase.debitAccount(command);
        });
        
        verify(accountRepositoryPort, never()).save(any());
    }
}
```

## 2. Integration Tests for Adapters

Test that adapters correctly implement the ports and map between domain and persistence.

### Example: ClientPersistenceAdapterTest

```java
package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.ClientJpaRepository;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ClientPersistenceAdapter.class)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ClientPersistenceAdapterTest {
    
    @Autowired
    private ClientPersistenceAdapter adapter;
    
    @Autowired
    private ClientJpaRepository repository;
    
    @Test
    void testSaveAndFind() {
        Client client = new Client("Test Client", "Address", "Phone", "email@test.com", ClientType.PRIVATE);
        
        Client saved = adapter.save(client);
        
        assertNotNull(saved.getId());
        
        Client found = adapter.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Client", found.getName());
    }
}
```

## 3. End-to-End Integration Tests

Test the full application through REST endpoints.

### Example: Updated ClientControllerIntegrationTest

```java
package com.simplecash.projet_akoum_mohamad.adapter.in.web.controller;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.ClientDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreateClientRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.ErrorResponse;
import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AdvisorJpaRepository;
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
    private AdvisorJpaRepository advisorRepository;
    
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/clients";
    }
    
    @Test
    void testGetAllClients() {
        ResponseEntity<ClientDTO[]> response = restTemplate.getForEntity(
            baseUrl, ClientDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGetClientById_Success() {
        Long existingClientId = 1L;
        
        ResponseEntity<ClientDTO> response = restTemplate.getForEntity(
            baseUrl + "/" + existingClientId, ClientDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existingClientId, response.getBody().getId());
    }
    
    @Test
    void testGetClientById_NotFound() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
            baseUrl + "/99999", ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
    }
    
    @Test
    void testCreateClient_Success() {
        Long advisorId = advisorRepository.findAll().stream()
            .findFirst()
            .map(a -> a.getId())
            .orElse(1L);
        
        CreateClientRequest request = new CreateClientRequest();
        request.setName("Test Client " + System.currentTimeMillis());
        request.setAddress("Test Address");
        request.setPhone("123-456-7890");
        request.setEmail("test" + System.currentTimeMillis() + "@test.com");
        request.setClientType(ClientType.PRIVATE);
        request.setAdvisorId(advisorId);
        
        ResponseEntity<ClientDTO> response = restTemplate.postForEntity(
            baseUrl, request, ClientDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(request.getName(), response.getBody().getName());
    }
}
```

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests "AccountUseCaseTest"
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

## Test Structure Recommendations

1. **Unit Tests** (`application.usecase` package)
   - Test use cases in isolation
   - Mock all port.out dependencies
   - Fast execution
   - No Spring context needed

2. **Adapter Tests** (`adapter.out.persistence.adapter` package)
   - Test persistence adapters with in-memory database
   - Use `@DataJpaTest` for JPA tests
   - Verify mapping between domain and entities

3. **Integration Tests** (`adapter.in.web.controller` package)
   - Test full application stack
   - Use `@SpringBootTest` with `RANDOM_PORT`
   - Test REST endpoints end-to-end

## Benefits of Hexagonal Architecture Testing

1. **Isolated Business Logic**: Test use cases without database or web framework
2. **Fast Unit Tests**: No Spring context needed for use case tests
3. **Easy Mocking**: Port interfaces are easy to mock
4. **Clear Boundaries**: Each layer can be tested independently
5. **Testability**: Core business logic is framework-independent

