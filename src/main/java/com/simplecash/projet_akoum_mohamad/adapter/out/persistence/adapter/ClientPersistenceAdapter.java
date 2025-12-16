package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.ClientEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.ClientJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.ClientRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClientPersistenceAdapter implements ClientRepositoryPort {
    
    private final ClientJpaRepository jpaRepository;
    private final ClientPersistenceMapper mapper;
    
    public ClientPersistenceAdapter(ClientJpaRepository jpaRepository, ClientPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Client save(Client client) {
        ClientEntity entity = mapper.toEntity(client);
        ClientEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Client> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Client> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Client> findByAdvisorId(Long advisorId) {
        return jpaRepository.findByAdvisorId(advisorId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Client client) {
        ClientEntity entity = mapper.toEntity(client);
        jpaRepository.delete(entity);
    }
}

