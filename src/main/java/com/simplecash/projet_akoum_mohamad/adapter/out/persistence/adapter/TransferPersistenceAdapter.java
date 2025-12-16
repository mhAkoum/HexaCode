package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.TransferPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.TransferJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.TransferRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferPersistenceAdapter implements TransferRepositoryPort {
    
    private final TransferJpaRepository jpaRepository;
    private final TransferPersistenceMapper mapper;
    
    public TransferPersistenceAdapter(TransferJpaRepository jpaRepository, TransferPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Transfer save(Transfer transfer) {
        com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity entity = mapper.toEntity(transfer);
        com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}

