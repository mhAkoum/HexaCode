package com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreateClientRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.UpdateClientRequest;
import com.simplecash.projet_akoum_mohamad.application.dto.CreateClientCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.UpdateClientCommand;

public class ClientWebMapper {
    
    public static CreateClientCommand toCommand(CreateClientRequest request) {
        return new CreateClientCommand(
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                request.getClientType(),
                request.getAdvisorId()
        );
    }
    
    public static UpdateClientCommand toCommand(UpdateClientRequest request) {
        return new UpdateClientCommand(
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                request.getClientType()
        );
    }
}

