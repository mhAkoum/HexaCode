package com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.AccountDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreditRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.DebitRequest;
import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountWebMapper {
    
    public AccountDTO toDTO(Account account) {
        return new AccountDTO(account);
    }
    
    public CreditAccountCommand toCommand(Long accountId, CreditRequest request) {
        return new CreditAccountCommand(accountId, request.getAmount());
    }
    
    public DebitAccountCommand toCommand(Long accountId, DebitRequest request) {
        return new DebitAccountCommand(accountId, request.getAmount());
    }
}

