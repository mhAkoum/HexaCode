package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Card;

public class CardPersistenceMapper {
    
    public static Card toDomain(CardEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Card card = new Card(entity.getCardType(), entity.getStatus());
        card.setId(entity.getId());
        
        // Client relationship handled separately
        
        return card;
    }
    
    public static CardEntity toEntity(Card domain) {
        if (domain == null) {
            return null;
        }
        
        CardEntity entity = new CardEntity(domain.getCardType(), domain.getStatus());
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        return entity;
    }
}

