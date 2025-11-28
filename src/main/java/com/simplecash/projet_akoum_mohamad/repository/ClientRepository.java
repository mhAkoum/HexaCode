package com.simplecash.projet_akoum_mohamad.repository;

import com.simplecash.projet_akoum_mohamad.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByAdvisorId(Long advisorId);
}

