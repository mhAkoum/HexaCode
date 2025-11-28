package com.simplecash.projet_akoum_mohamad.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    
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
    private Advisor advisor;
    
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
}

