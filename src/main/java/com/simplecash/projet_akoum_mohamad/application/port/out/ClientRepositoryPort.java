package com.simplecash.projet_akoum_mohamad.application.port.out;

import com.simplecash.projet_akoum_mohamad.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {
    Client save(Client client);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    List<Client> findByAdvisorId(Long advisorId);
    void delete(Client client);
}

