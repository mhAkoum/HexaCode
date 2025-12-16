package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "email")
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;
    
    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private AdvisorEntity advisor;
    
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private CurrentAccountEntity currentAccount;
    
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private SavingsAccountEntity savingsAccount;
    
    @OneToMany(mappedBy = "client", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CardEntity> cards = new ArrayList<>();
    
    public ClientEntity() {
    }
    
    public ClientEntity(String name, String address, String phone, String email, ClientType clientType) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.clientType = clientType;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public ClientType getClientType() {
        return clientType;
    }
    
    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
    
    public AdvisorEntity getAdvisor() {
        return advisor;
    }
    
    public void setAdvisor(AdvisorEntity advisor) {
        this.advisor = advisor;
    }
    
    public CurrentAccountEntity getCurrentAccount() {
        return currentAccount;
    }
    
    public void setCurrentAccount(CurrentAccountEntity currentAccount) {
        this.currentAccount = currentAccount;
    }
    
    public SavingsAccountEntity getSavingsAccount() {
        return savingsAccount;
    }
    
    public void setSavingsAccount(SavingsAccountEntity savingsAccount) {
        this.savingsAccount = savingsAccount;
    }
    
    public List<CardEntity> getCards() {
        return cards;
    }
    
    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }
    
    public void addCard(CardEntity card) {
        cards.add(card);
        card.setClient(this);
    }
    
    public void removeCard(CardEntity card) {
        cards.remove(card);
        card.setClient(null);
    }
}

