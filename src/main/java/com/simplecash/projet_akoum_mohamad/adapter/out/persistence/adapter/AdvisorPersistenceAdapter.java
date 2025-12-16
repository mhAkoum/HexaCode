package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AdvisorEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AdvisorPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AdvisorJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.AdvisorRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class AdvisorPersistenceAdapter implements AdvisorRepositoryPort {
    
    private final AdvisorJpaRepository repository;
    
    public AdvisorPersistenceAdapter(AdvisorJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Advisor> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomainWithClients);
    }
    
    @Override
    public List<Advisor> findAll() {
        return repository.findAll().stream()
                .map(this::toDomainWithClients)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Advisor> findByAgencyId(Long agencyId) {
        return repository.findByAgencyId(agencyId).stream()
                .map(this::toDomainWithClients)
                .collect(Collectors.toList());
    }
    
    private Advisor toDomainWithClients(AdvisorEntity entity) {
        Advisor advisor = AdvisorPersistenceMapper.toDomain(entity);
        
        // Load clients for the advisor
        if (entity.getClients() != null && !entity.getClients().isEmpty()) {
            advisor.setClients(entity.getClients().stream()
                    .map(com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper::toDomain)
                    .collect(Collectors.toList()));
            advisor.getClients().forEach(client -> client.setAdvisor(advisor));
        }
        
        return advisor;
    }
}

