package com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreditRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.DebitRequest;
import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;

public class AccountWebMapper {
    
    public static CreditAccountCommand toCommand(Long accountId, CreditRequest request) {
        return new CreditAccountCommand(accountId, request.getAmount());
    }
    
    public static DebitAccountCommand toCommand(Long accountId, DebitRequest request) {
        return new DebitAccountCommand(accountId, request.getAmount());
    }
}

