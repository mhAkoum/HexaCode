package com.simplecash.projet_akoum_mohamad.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advisors")
public class Advisor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;
    
    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients = new ArrayList<>();
    
    public Advisor() {
    }
    
    public Advisor(String name, String email) {
        this.name = name;
        this.email = email;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Agency getAgency() {
        return agency;
    }
    
    public void setAgency(Agency agency) {
        this.agency = agency;
    }
    
    public List<Client> getClients() {
        return clients;
    }
    
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
    
    public void addClient(Client client) {
        clients.add(client);
        client.setAdvisor(this);
    }
    
    public void removeClient(Client client) {
        clients.remove(client);
        client.setAdvisor(null);
    }
}

