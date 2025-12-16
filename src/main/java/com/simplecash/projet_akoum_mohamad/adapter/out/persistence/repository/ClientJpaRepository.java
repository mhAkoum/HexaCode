package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    List<ClientEntity> findByAdvisorId(Long advisorId);
}

