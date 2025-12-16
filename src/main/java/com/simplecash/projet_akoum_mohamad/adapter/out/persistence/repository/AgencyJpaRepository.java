package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.AgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyJpaRepository extends JpaRepository<AgencyEntity, Long> {
}

