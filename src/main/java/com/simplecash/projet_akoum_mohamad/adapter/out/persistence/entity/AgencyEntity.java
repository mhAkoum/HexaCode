package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agencies")
public class AgencyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", length = 5, unique = true, nullable = false)
    private String code;
    
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    
    @OneToOne(mappedBy = "agency", cascade = CascadeType.ALL, orphanRemoval = true)
    private ManagerEntity manager;
    
    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdvisorEntity> advisors = new ArrayList<>();
    
    public AgencyEntity() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    
    public ManagerEntity getManager() {
        return manager;
    }
    
    public void setManager(ManagerEntity manager) {
        this.manager = manager;
    }
    
    public List<AdvisorEntity> getAdvisors() {
        return advisors;
    }
    
    public void setAdvisors(List<AdvisorEntity> advisors) {
        this.advisors = advisors;
    }
}

