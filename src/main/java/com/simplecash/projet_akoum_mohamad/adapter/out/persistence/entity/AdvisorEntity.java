package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advisors")
public class AdvisorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private AgencyEntity agency;
    
    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientEntity> clients = new ArrayList<>();
    
    public AdvisorEntity() {
    }
    
    public AdvisorEntity(String name, String email) {
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
    
    public AgencyEntity getAgency() {
        return agency;
    }
    
    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }
    
    public List<ClientEntity> getClients() {
        return clients;
    }
    
    public void setClients(List<ClientEntity> clients) {
        this.clients = clients;
    }
    
    public void addClient(ClientEntity client) {
        clients.add(client);
        client.setAdvisor(this);
    }
    
    public void removeClient(ClientEntity client) {
        clients.remove(client);
        client.setAdvisor(null);
    }
}

