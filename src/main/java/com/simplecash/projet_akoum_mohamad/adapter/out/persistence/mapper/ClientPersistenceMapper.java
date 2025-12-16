package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.ClientEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientPersistenceMapper {
    
    public Client toDomain(ClientEntity entity) {
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
        
        return client;
    }
    
    public ClientEntity toEntity(Client domain) {
        if (domain == null) {
            return null;
        }
        
        ClientEntity entity = new ClientEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setPhone(domain.getPhone());
        entity.setEmail(domain.getEmail());
        entity.setClientType(domain.getClientType());
        
        return entity;
    }
}

