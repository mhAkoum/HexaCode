package com.simplecash.projet_akoum_mohamad.repository;

import com.simplecash.projet_akoum_mohamad.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByAgencyId(Long agencyId);
}

