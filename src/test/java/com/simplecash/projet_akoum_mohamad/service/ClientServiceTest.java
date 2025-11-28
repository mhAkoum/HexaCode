package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Advisor;
import com.simplecash.projet_akoum_mohamad.domain.Client;
import com.simplecash.projet_akoum_mohamad.domain.ClientType;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorFullException;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorNotFoundException;
import com.simplecash.projet_akoum_mohamad.repository.AdvisorRepository;
import com.simplecash.projet_akoum_mohamad.repository.CardRepository;
import com.simplecash.projet_akoum_mohamad.repository.ClientRepository;
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
class ClientServiceTest {
    
    @Mock
    private ClientRepository clientRepository;
    
    @Mock
    private AdvisorRepository advisorRepository;
    
    @Mock
    private CardRepository cardRepository;
    
    @InjectMocks
    private ClientService clientService;
    
    private Advisor testAdvisor;
    private List<Client> advisorClients;
    
    @BeforeEach
    void setUp() {
        testAdvisor = new Advisor();
        testAdvisor.setId(1L);
        testAdvisor.setName("Test Advisor");
        advisorClients = new ArrayList<>();
        testAdvisor.setClients(advisorClients);
    }
    
    @Test
    void testCreateClient_Success() {
        when(advisorRepository.findById(1L)).thenReturn(Optional.of(testAdvisor));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(1L);
            return client;
        });
        
        Client result = clientService.createClient(
                "Test Client",
                "Test Address",
                "123-456-7890",
                "test@test.com",
                ClientType.PRIVATE,
                1L
        );
        
        assertNotNull(result);
        assertEquals("Test Client", result.getName());
        assertEquals(ClientType.PRIVATE, result.getClientType());
        verify(clientRepository, times(1)).save(any(Client.class));
    }
    
    @Test
    void testCreateClient_AdvisorNotFound() {
        when(advisorRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(AdvisorNotFoundException.class, () -> {
            clientService.createClient(
                    "Test Client",
                    "Test Address",
                    "123-456-7890",
                    "test@test.com",
                    ClientType.PRIVATE,
                    999L
            );
        });
        
        verify(clientRepository, never()).save(any(Client.class));
    }
    
    @Test
    void testCreateClient_AdvisorFull() {
        for (int i = 0; i < 10; i++) {
            Client client = new Client();
            client.setId((long) i);
            advisorClients.add(client);
        }
        
        when(advisorRepository.findById(1L)).thenReturn(Optional.of(testAdvisor));
        
        assertThrows(AdvisorFullException.class, () -> {
            clientService.createClient(
                    "Test Client",
                    "Test Address",
                    "123-456-7890",
                    "test@test.com",
                    ClientType.PRIVATE,
                    1L
            );
        });
        
        verify(clientRepository, never()).save(any(Client.class));
    }
    
    @Test
    void testGetClientById_Success() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        
        Client result = clientService.getClientById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Client", result.getName());
    }
    
    @Test
    void testGetAllClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());
        
        when(clientRepository.findAll()).thenReturn(clients);
        
        List<Client> result = clientService.getAllClients();
        
        assertEquals(2, result.size());
    }
}

