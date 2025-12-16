package com.simplecash.projet_akoum_mohamad.application.port.out;

import com.simplecash.projet_akoum_mohamad.domain.model.Account;

import java.util.Optional;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findById(Long id);
    Optional<Account> findByAccountNumber(String accountNumber);
}

