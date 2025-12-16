package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferJpaRepository extends JpaRepository<TransferEntity, Long> {
}

