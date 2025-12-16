package com.simplecash.projet_akoum_mohamad.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Agency {
    
    private Long id;
    private String code;
    private LocalDate creationDate;
    private Manager manager;
    private List<Advisor> advisors = new ArrayList<>();
    
    public Agency() {
    }
    
    public Agency(String code, LocalDate creationDate) {
        this.code = code;
        this.creationDate = creationDate;
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
    
    public Manager getManager() {
        return manager;
    }
    
    public void setManager(Manager manager) {
        this.manager = manager;
        if (manager != null) {
            manager.setAgency(this);
        }
    }
    
    public List<Advisor> getAdvisors() {
        return advisors;
    }
    
    public void setAdvisors(List<Advisor> advisors) {
        this.advisors = advisors;
    }
    
    public void addAdvisor(Advisor advisor) {
        advisors.add(advisor);
        advisor.setAgency(this);
    }
    
    public void removeAdvisor(Advisor advisor) {
        advisors.remove(advisor);
        advisor.setAgency(null);
    }
}

