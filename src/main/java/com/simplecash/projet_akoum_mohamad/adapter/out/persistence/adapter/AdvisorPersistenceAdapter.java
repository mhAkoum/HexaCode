package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.AdvisorPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.AdvisorJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.AdvisorRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AdvisorPersistenceAdapter implements AdvisorRepositoryPort {
    
    private final AdvisorJpaRepository jpaRepository;
    private final AdvisorPersistenceMapper mapper;
    
    public AdvisorPersistenceAdapter(AdvisorJpaRepository jpaRepository, AdvisorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Optional<Advisor> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Advisor> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Advisor> findByAgencyId(Long agencyId) {
        return jpaRepository.findByAgencyId(agencyId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

