package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.CardPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.CardJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.CardRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Card;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class CardPersistenceAdapter implements CardRepositoryPort {
    
    private final CardJpaRepository repository;
    
    public CardPersistenceAdapter(CardJpaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Card save(Card card) {
        CardEntity entity = CardPersistenceMapper.toEntity(card);
        
        if (card.getClient() != null) {
            entity.setClient(com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper
                    .toEntity(card.getClient()));
        }
        
        CardEntity saved = repository.save(entity);
        Card domain = CardPersistenceMapper.toDomain(saved);
        if (saved.getClient() != null) {
            domain.setClient(com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.ClientPersistenceMapper
                    .toDomain(saved.getClient()));
        }
        return domain;
    }
    
    @Override
    public List<Card> findByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream()
                .map(CardPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}

