package com.simplecash.projet_akoum_mohamad.adapter.in.web.controller;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.AccountDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreditRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.DebitRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper.AccountWebMapper;
import com.simplecash.projet_akoum_mohamad.application.port.in.AccountUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountUseCase accountUseCase;
    private final AccountWebMapper mapper;
    
    public AccountController(AccountUseCase accountUseCase, AccountWebMapper mapper) {
        this.accountUseCase = accountUseCase;
        this.mapper = mapper;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = mapper.toDTO(accountUseCase.getAccountById(id));
        return ResponseEntity.ok(account);
    }
    
    @PostMapping("/{id}/credit")
    public ResponseEntity<AccountDTO> creditAccount(
            @PathVariable Long id,
            @RequestBody CreditRequest request) {
        AccountDTO account = mapper.toDTO(accountUseCase.creditAccount(mapper.toCommand(id, request)));
        return ResponseEntity.ok(account);
    }
    
    @PostMapping("/{id}/debit")
    public ResponseEntity<AccountDTO> debitAccount(
            @PathVariable Long id,
            @RequestBody DebitRequest request) {
        AccountDTO account = mapper.toDTO(accountUseCase.debitAccount(mapper.toCommand(id, request)));
        return ResponseEntity.ok(account);
    }
}

