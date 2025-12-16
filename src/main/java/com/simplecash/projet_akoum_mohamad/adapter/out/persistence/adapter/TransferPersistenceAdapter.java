package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.TransferPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.TransferJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.TransferRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TransferPersistenceAdapter implements TransferRepositoryPort {
    
    private final TransferJpaRepository repository;
    
    public TransferPersistenceAdapter(TransferJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Transfer save(Transfer transfer) {
        TransferEntity entity = TransferPersistenceMapper.toEntity(transfer);
        TransferEntity saved = repository.save(entity);
        return TransferPersistenceMapper.toDomain(saved);
    }
}

