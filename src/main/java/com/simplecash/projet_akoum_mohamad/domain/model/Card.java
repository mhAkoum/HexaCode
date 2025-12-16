package com.simplecash.projet_akoum_mohamad.domain.model;

public class Card {
    
    private Long id;
    private CardType cardType;
    private CardStatus status;
    private Client client;
    
    public Card() {
    }
    
    public Card(CardType cardType, CardStatus status) {
        this.cardType = cardType;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public CardType getCardType() {
        return cardType;
    }
    
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
    
    public CardStatus getStatus() {
        return status;
    }
    
    public void setStatus(CardStatus status) {
        this.status = status;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
}

