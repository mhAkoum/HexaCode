package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.CreateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.UpdateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.port.out.AdvisorRepositoryPort;
import com.simplecash.projet_akoum_mohamad.application.port.out.CardRepositoryPort;
import com.simplecash.projet_akoum_mohamad.application.port.out.ClientRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorFullException;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {
    
    @Mock
    private ClientRepositoryPort clientRepositoryPort;
    
    @Mock
    private AdvisorRepositoryPort advisorRepositoryPort;
    
    @Mock
    private CardRepositoryPort cardRepositoryPort;
    
    @InjectMocks
    private ClientUseCaseImpl clientUseCase;
    
    private Advisor advisor;
    
    @BeforeEach
    void setUp() {
        advisor = new Advisor("Test Advisor", "advisor@test.com");
        advisor.setId(1L);
        advisor.setClients(new ArrayList<>());
    }
    
    @Test
    void testCreateClient_Success() {
        when(advisorRepositoryPort.findById(1L))
            .thenReturn(Optional.of(advisor));
        when(clientRepositoryPort.save(any(Client.class)))
            .thenAnswer(invocation -> {
                Client client = invocation.getArgument(0);
                client.setId(1L);
                return client;
            });
        
        CreateClientCommand command = new CreateClientCommand(
            "John Doe", "123 Main St", "555-1234", "john@test.com", ClientType.PRIVATE, 1L);
        
        Client created = clientUseCase.createClient(command);
        
        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        verify(clientRepositoryPort, times(1)).save(any(Client.class));
    }
    
    @Test
    void testCreateClient_AdvisorNotFound() {
        when(advisorRepositoryPort.findById(999L))
            .thenReturn(Optional.empty());
        
        CreateClientCommand command = new CreateClientCommand(
            "John Doe", "123 Main St", "555-1234", "john@test.com", ClientType.PRIVATE, 999L);
        
        assertThrows(AdvisorNotFoundException.class, () -> {
            clientUseCase.createClient(command);
        });
    }
    
    @Test
    void testCreateClient_AdvisorFull() {
        // Create 10 clients for the advisor
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Client client = new Client("Client " + i, "Address", "Phone", "email@test.com", ClientType.PRIVATE);
            clients.add(client);
        }
        advisor.setClients(clients);
        
        when(advisorRepositoryPort.findById(1L))
            .thenReturn(Optional.of(advisor));
        
        CreateClientCommand command = new CreateClientCommand(
            "John Doe", "123 Main St", "555-1234", "john@test.com", ClientType.PRIVATE, 1L);
        
        assertThrows(AdvisorFullException.class, () -> {
            clientUseCase.createClient(command);
        });
        
        verify(clientRepositoryPort, never()).save(any());
    }
    
    @Test
    void testGetClientById_NotFound() {
        when(clientRepositoryPort.findById(999L))
            .thenReturn(Optional.empty());
        
        assertThrows(ClientNotFoundException.class, () -> {
            clientUseCase.getClientById(999L);
        });
    }
    
    @Test
    void testUpdateClient_Success() {
        Client client = new Client("John Doe", "Old Address", "555-1234", "john@test.com", ClientType.PRIVATE);
        client.setId(1L);
        
        when(clientRepositoryPort.findById(1L))
            .thenReturn(Optional.of(client));
        when(clientRepositoryPort.save(any(Client.class)))
            .thenReturn(client);
        
        UpdateClientCommand command = new UpdateClientCommand(
            "Jane Doe", "New Address", "555-5678", "jane@test.com", ClientType.BUSINESS);
        
        Client updated = clientUseCase.updateClient(1L, command);
        
        assertEquals("Jane Doe", updated.getName());
        assertEquals("New Address", updated.getAddress());
        verify(clientRepositoryPort, times(1)).save(client);
    }
}

