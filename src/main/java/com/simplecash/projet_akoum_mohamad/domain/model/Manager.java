package com.simplecash.projet_akoum_mohamad.domain.model;

public class Manager {
    
    private Long id;
    private String name;
    private String email;
    private Agency agency;
    
    public Manager() {
    }
    
    public Manager(String name, String email) {
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
}

