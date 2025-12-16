package com.simplecash.projet_akoum_mohamad.application.port.out;

import com.simplecash.projet_akoum_mohamad.domain.model.Advisor;

import java.util.List;
import java.util.Optional;

public interface AdvisorRepositoryPort {
    Optional<Advisor> findById(Long id);
    List<Advisor> findAll();
    List<Advisor> findByAgencyId(Long agencyId);
}

