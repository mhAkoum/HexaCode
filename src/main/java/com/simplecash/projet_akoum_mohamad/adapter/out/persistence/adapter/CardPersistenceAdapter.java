package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.adapter;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.mapper.CardPersistenceMapper;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.CardJpaRepository;
import com.simplecash.projet_akoum_mohamad.application.port.out.CardRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Card;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardPersistenceAdapter implements CardRepositoryPort {
    
    private final CardJpaRepository jpaRepository;
    private final CardPersistenceMapper mapper;
    
    public CardPersistenceAdapter(CardJpaRepository jpaRepository, CardPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Card save(Card card) {
        com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity entity = mapper.toEntity(card);
        com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public List<Card> findByClientId(Long clientId) {
        return jpaRepository.findByClientId(clientId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

