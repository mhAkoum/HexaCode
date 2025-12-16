package com.simplecash.projet_akoum_mohamad.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Client {
    
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private ClientType clientType;
    private Advisor advisor;
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;
    private List<Card> cards = new ArrayList<>();
    
    public Client() {
    }
    
    public Client(String name, String address, String phone, String email, ClientType clientType) {
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
    
    public Advisor getAdvisor() {
        return advisor;
    }
    
    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }
    
    public CurrentAccount getCurrentAccount() {
        return currentAccount;
    }
    
    public void setCurrentAccount(CurrentAccount currentAccount) {
        this.currentAccount = currentAccount;
    }
    
    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }
    
    public void setSavingsAccount(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }
    
    public List<Card> getCards() {
        return cards;
    }
    
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    
    public void addCard(Card card) {
        cards.add(card);
        card.setClient(this);
    }
    
    public void removeCard(Card card) {
        cards.remove(card);
        card.setClient(null);
    }
}

