package com.simplecash.projet_akoum_mohamad.application.port.in;

import com.simplecash.projet_akoum_mohamad.application.dto.CreateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.UpdateClientCommand;
import com.simplecash.projet_akoum_mohamad.domain.model.Client;

import java.util.List;

public interface ClientUseCase {
    List<Client> getAllClients();
    Client getClientById(Long id);
    Client createClient(CreateClientCommand command);
    Client updateClient(Long id, UpdateClientCommand command);
    void deleteClient(Long id);
}

