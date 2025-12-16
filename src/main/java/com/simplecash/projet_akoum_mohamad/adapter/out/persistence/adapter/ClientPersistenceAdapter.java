package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.ClientEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AccountPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AdvisorPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.CardPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.ClientJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.ClientRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class ClientPersistenceAdapter implements ClientRepositoryPort {
    
    private final ClientJpaRepository repository;
    
    public ClientPersistenceAdapter(ClientJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Client save(Client client) {
        ClientEntity entity = ClientPersistenceMapper.toEntity(client);
        
        // Handle relationships
        if (client.getAdvisor() != null) {
            entity.setAdvisor(AdvisorPersistenceMapper.toEntity(client.getAdvisor()));
        }
        if (client.getCurrentAccount() != null) {
            entity.setCurrentAccount((com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CurrentAccountEntity) 
                    AccountPersistenceMapper.toEntity(client.getCurrentAccount()));
            entity.getCurrentAccount().setClient(entity);
        }
        if (client.getSavingsAccount() != null) {
            entity.setSavingsAccount((com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.SavingsAccountEntity) 
                    AccountPersistenceMapper.toEntity(client.getSavingsAccount()));
            entity.getSavingsAccount().setClient(entity);
        }
        if (client.getCards() != null && !client.getCards().isEmpty()) {
            entity.setCards(client.getCards().stream()
                    .map(CardPersistenceMapper::toEntity)
                    .collect(Collectors.toList()));
            entity.getCards().forEach(card -> card.setClient(entity));
        }
        
        ClientEntity saved = repository.save(entity);
        return toDomainWithRelations(saved);
    }
    
    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomainWithRelations);
    }
    
    @Override
    public List<Client> findAll() {
        return repository.findAll().stream()
                .map(this::toDomainWithRelations)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Client> findByAdvisorId(Long advisorId) {
        return repository.findByAdvisorId(advisorId).stream()
                .map(this::toDomainWithRelations)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Client client) {
        repository.deleteById(client.getId());
    }
    
    private Client toDomainWithRelations(ClientEntity entity) {
        Client client = ClientPersistenceMapper.toDomain(entity);
        
        if (entity.getAdvisor() != null) {
            client.setAdvisor(AdvisorPersistenceMapper.toDomain(entity.getAdvisor()));
        }
        if (entity.getCurrentAccount() != null) {
            client.setCurrentAccount((com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount) 
                    AccountPersistenceMapper.toDomain(entity.getCurrentAccount()));
            client.getCurrentAccount().setClient(client);
        }
        if (entity.getSavingsAccount() != null) {
            client.setSavingsAccount((com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount) 
                    AccountPersistenceMapper.toDomain(entity.getSavingsAccount()));
            client.getSavingsAccount().setClient(client);
        }
        if (entity.getCards() != null && !entity.getCards().isEmpty()) {
            client.setCards(entity.getCards().stream()
                    .map(CardPersistenceMapper::toDomain)
                    .collect(Collectors.toList()));
            client.getCards().forEach(card -> card.setClient(client));
        }
        
        return client;
    }
}

