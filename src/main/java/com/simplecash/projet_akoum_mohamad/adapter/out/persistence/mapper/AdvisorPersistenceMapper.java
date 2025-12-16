package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AdvisorEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;

public class AdvisorPersistenceMapper {
    
    public static Advisor toDomain(AdvisorEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Advisor advisor = new Advisor(entity.getName(), entity.getEmail());
        advisor.setId(entity.getId());
        
        // Relationships handled separately
        
        return advisor;
    }
    
    public static AdvisorEntity toEntity(Advisor domain) {
        if (domain == null) {
            return null;
        }
        
        AdvisorEntity entity = new AdvisorEntity(domain.getName(), domain.getEmail());
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        return entity;
    }
}

