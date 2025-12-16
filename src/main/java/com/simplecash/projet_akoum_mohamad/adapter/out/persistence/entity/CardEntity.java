package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class CardEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private com.simplecash.projet_akoum_mohamad.domain.model.CardType cardType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private com.simplecash.projet_akoum_mohamad.domain.model.CardStatus status;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
    
    public CardEntity() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public com.simplecash.projet_akoum_mohamad.domain.model.CardType getCardType() {
        return cardType;
    }
    
    public void setCardType(com.simplecash.projet_akoum_mohamad.domain.model.CardType cardType) {
        this.cardType = cardType;
    }
    
    public com.simplecash.projet_akoum_mohamad.domain.model.CardStatus getStatus() {
        return status;
    }
    
    public void setStatus(com.simplecash.projet_akoum_mohamad.domain.model.CardStatus status) {
        this.status = status;
    }
    
    public ClientEntity getClient() {
        return client;
    }
    
    public void setClient(ClientEntity client) {
        this.client = client;
    }
}

