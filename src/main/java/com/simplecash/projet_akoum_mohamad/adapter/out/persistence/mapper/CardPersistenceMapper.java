package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity;
import com.simplecash.projet_akoum_mohamad.domain.model.Card;
import org.springframework.stereotype.Component;

@Component
public class CardPersistenceMapper {
    
    public Card toDomain(CardEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Card card = new Card(entity.getCardType(), entity.getStatus());
        card.setId(entity.getId());
        
        return card;
    }
    
    public CardEntity toEntity(Card domain) {
        if (domain == null) {
            return null;
        }
        
        CardEntity entity = new CardEntity();
        entity.setId(domain.getId());
        entity.setCardType(domain.getCardType());
        entity.setStatus(domain.getStatus());
        
        return entity;
    }
}

