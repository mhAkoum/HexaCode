package com.simplecash.projet_akoum_mohamad.application.port.out;

import com.simplecash.projet_akoum_mohamad.domain.model.Card;

import java.util.List;

public interface CardRepositoryPort {
    Card save(Card card);
    List<Card> findByClientId(Long clientId);
}

