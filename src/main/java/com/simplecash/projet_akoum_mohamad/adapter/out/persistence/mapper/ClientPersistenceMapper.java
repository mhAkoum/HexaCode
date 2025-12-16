package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.ClientEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;

public class ClientPersistenceMapper {
    
    public static Client toDomain(ClientEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Client client = new Client(
                entity.getName(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getClientType()
        );
        client.setId(entity.getId());
        
        // Note: Relationships are handled separately by adapters to avoid circular dependencies
        // The adapter will set advisor, accounts, and cards after mapping
        
        return client;
    }
    
    public static ClientEntity toEntity(Client domain) {
        if (domain == null) {
            return null;
        }
        
        ClientEntity entity = new ClientEntity(
                domain.getName(),
                domain.getAddress(),
                domain.getPhone(),
                domain.getEmail(),
                domain.getClientType()
        );
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        // Note: Relationships are handled separately by adapters
        
        return entity;
    }
}

