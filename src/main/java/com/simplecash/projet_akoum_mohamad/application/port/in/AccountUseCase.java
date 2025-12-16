package com.simplecash.projet_akoum_mohamad.application.port.in;

import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;

public interface AccountUseCase {
    Account getAccountById(Long id);
    Account getAccountByNumber(String accountNumber);
    Account creditAccount(CreditAccountCommand command);
    Account debitAccount(DebitAccountCommand command);
}

