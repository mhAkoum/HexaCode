package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferPersistenceMapper {
    
    @Autowired
    private AccountPersistenceMapper accountMapper;
    
    public Transfer toDomain(TransferEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Transfer transfer = new Transfer(
                accountMapper.toDomain(entity.getSourceAccount()),
                accountMapper.toDomain(entity.getTargetAccount()),
                entity.getAmount(),
                entity.getTimestamp(),
                entity.getStatus()
        );
        transfer.setId(entity.getId());
        
        return transfer;
    }
    
    public TransferEntity toEntity(Transfer domain) {
        if (domain == null) {
            return null;
        }
        
        TransferEntity entity = new TransferEntity();
        entity.setId(domain.getId());
        entity.setSourceAccount(accountMapper.toEntity(domain.getSourceAccount()));
        entity.setTargetAccount(accountMapper.toEntity(domain.getTargetAccount()));
        entity.setAmount(domain.getAmount());
        entity.setTimestamp(domain.getTimestamp());
        entity.setStatus(domain.getStatus());
        
        return entity;
    }
}

