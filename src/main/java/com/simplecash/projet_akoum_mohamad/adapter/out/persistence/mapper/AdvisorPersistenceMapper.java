package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AdvisorEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;
import org.springframework.stereotype.Component;

@Component
public class AdvisorPersistenceMapper {
    
    public Advisor toDomain(AdvisorEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Advisor advisor = new Advisor(entity.getName(), entity.getEmail());
        advisor.setId(entity.getId());
        
        return advisor;
    }
    
    public AdvisorEntity toEntity(Advisor domain) {
        if (domain == null) {
            return null;
        }
        
        AdvisorEntity entity = new AdvisorEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        
        return entity;
    }
}

