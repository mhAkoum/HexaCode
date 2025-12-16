package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "managers")
public class ManagerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @OneToOne
    @JoinColumn(name = "agency_id", unique = true, nullable = false)
    private AgencyEntity agency;
    
    public ManagerEntity() {
    }
    
    public ManagerEntity(String name, String email) {
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
}

