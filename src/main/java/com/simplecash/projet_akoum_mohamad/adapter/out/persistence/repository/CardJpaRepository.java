package com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByClientId(Long clientId);
}

