package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;

public class TransferPersistenceMapper {
    
    public static Transfer toDomain(TransferEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Transfer transfer = new Transfer(
                AccountPersistenceMapper.toDomain(entity.getSourceAccount()),
                AccountPersistenceMapper.toDomain(entity.getTargetAccount()),
                entity.getAmount(),
                entity.getTimestamp(),
                entity.getStatus()
        );
        transfer.setId(entity.getId());
        
        return transfer;
    }
    
    public static TransferEntity toEntity(Transfer domain) {
        if (domain == null) {
            return null;
        }
        
        TransferEntity entity = new TransferEntity(
                AccountPersistenceMapper.toEntity(domain.getSourceAccount()),
                AccountPersistenceMapper.toEntity(domain.getTargetAccount()),
                domain.getAmount(),
                domain.getTimestamp(),
                domain.getStatus()
        );
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        return entity;
    }
}

