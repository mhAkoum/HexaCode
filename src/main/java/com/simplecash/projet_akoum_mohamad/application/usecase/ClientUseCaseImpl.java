package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.CreateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.UpdateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.port.in.ClientUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.AdvisorRepositoryPort;
import com.simplecash.projet_akoum_mohamad.application.port.out.CardRepositoryPort;
import com.simplecash.projet_akoum_mohamad.application.port.out.ClientRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;
import com.simplecash.projet_akoum_mohamad.domain.model.Card;
import com.simplecash.projet_akoum_mohamad.domain.model.CardStatus;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import com.simplecash.projet_akoum_mohamad.exception.AccountBalanceNotZeroException;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorFullException;
import com.simplecash.projet_akoum_mohamad.exception.AdvisorNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.ClientNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class ClientUseCaseImpl implements ClientUseCase {
    
    private static final int MAX_CLIENTS_PER_ADVISOR = 10;
    
    private final ClientRepositoryPort clientRepository;
    private final AdvisorRepositoryPort advisorRepository;
    private final CardRepositoryPort cardRepository;
    
    public ClientUseCaseImpl(ClientRepositoryPort clientRepository, 
                            AdvisorRepositoryPort advisorRepository,
                            CardRepositoryPort cardRepository) {
        this.clientRepository = clientRepository;
        this.advisorRepository = advisorRepository;
        this.cardRepository = cardRepository;
    }
    
    @Override
    public Client createClient(CreateClientCommand command) {
        Advisor advisor = advisorRepository.findById(command.getAdvisorId())
                .orElseThrow(() -> new AdvisorNotFoundException(command.getAdvisorId()));
        
        if (advisor.getClients().size() >= MAX_CLIENTS_PER_ADVISOR) {
            throw new AdvisorFullException(command.getAdvisorId());
        }
        
        Client client = new Client(
                command.getName(),
                command.getAddress(),
                command.getPhone(),
                command.getEmail(),
                command.getClientType()
        );
        client.setAdvisor(advisor);
        advisor.addClient(client);
        
        return clientRepository.save(client);
    }
    
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
    
    @Override
    public Client updateClient(Long id, UpdateClientCommand command) {
        Client client = getClientById(id);
        
        if (command.getName() != null) {
            client.setName(command.getName());
        }
        if (command.getAddress() != null) {
            client.setAddress(command.getAddress());
        }
        if (command.getPhone() != null) {
            client.setPhone(command.getPhone());
        }
        if (command.getEmail() != null) {
            client.setEmail(command.getEmail());
        }
        if (command.getClientType() != null) {
            client.setClientType(command.getClientType());
        }
        
        return clientRepository.save(client);
    }
    
    @Override
    public void deleteClient(Long id) {
        Client client = getClientById(id);
        Advisor advisor = client.getAdvisor();
        
        if (client.getCurrentAccount() != null) {
            BigDecimal currentBalance = client.getCurrentAccount().getBalance();
            if (currentBalance.compareTo(BigDecimal.ZERO) != 0) {
                throw new AccountBalanceNotZeroException(
                    id, 
                    "Current", 
                    client.getCurrentAccount().getAccountNumber()
                );
            }
        }
        
        if (client.getSavingsAccount() != null) {
            BigDecimal savingsBalance = client.getSavingsAccount().getBalance();
            if (savingsBalance.compareTo(BigDecimal.ZERO) != 0) {
                throw new AccountBalanceNotZeroException(
                    id, 
                    "Savings", 
                    client.getSavingsAccount().getAccountNumber()
                );
            }
        }
        
        for (Card card : client.getCards()) {
            card.setStatus(CardStatus.INACTIVE);
            cardRepository.save(card);
        }
        
        if (advisor != null) {
            advisor.removeClient(client);
        }
        
        clientRepository.delete(client);
    }
}

