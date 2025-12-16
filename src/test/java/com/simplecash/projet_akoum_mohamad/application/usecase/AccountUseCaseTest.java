package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand;
import com.simplecash.projet_akoum_mohamad.application.port.out.AccountRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.model.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {
    
    @Mock
    private AccountRepositoryPort accountRepositoryPort;
    
    @InjectMocks
    private AccountUseCaseImpl accountUseCase;
    
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;
    
    @BeforeEach
    void setUp() {
        currentAccount = new CurrentAccount("CA001", 
            new BigDecimal("1000.00"), 
            LocalDate.now(), 
            new BigDecimal("1000.00"));
        currentAccount.setId(1L);
        
        savingsAccount = new SavingsAccount("SA001", 
            new BigDecimal("500.00"), 
            LocalDate.now(), 
            new BigDecimal("3.00"));
        savingsAccount.setId(2L);
    }
    
    @Test
    void testCreditAccount_Success() {
        when(accountRepositoryPort.findById(1L))
            .thenReturn(Optional.of(currentAccount));
        when(accountRepositoryPort.save(any(CurrentAccount.class)))
            .thenReturn(currentAccount);
        
        CreditAccountCommand command = new CreditAccountCommand(1L, new BigDecimal("200.00"));
        accountUseCase.creditAccount(command);
        
        assertEquals(new BigDecimal("1200.00"), currentAccount.getBalance());
        verify(accountRepositoryPort, times(1)).save(currentAccount);
    }
    
    @Test
    void testCreditAccount_InvalidAmount() {
        CreditAccountCommand command = new CreditAccountCommand(1L, BigDecimal.ZERO);
        
        assertThrows(IllegalArgumentException.class, () -> {
            accountUseCase.creditAccount(command);
        });
        
        verify(accountRepositoryPort, never()).save(any());
    }
    
    @Test
    void testDebitAccount_CurrentAccount_WithinOverdraft() {
        currentAccount.setBalance(new BigDecimal("500.00"));
        when(accountRepositoryPort.findById(1L))
            .thenReturn(Optional.of(currentAccount));
        when(accountRepositoryPort.save(any(CurrentAccount.class)))
            .thenReturn(currentAccount);
        
        DebitAccountCommand command = new DebitAccountCommand(1L, new BigDecimal("1200.00"));
        accountUseCase.debitAccount(command);
        
        assertEquals(new BigDecimal("-700.00"), currentAccount.getBalance());
        verify(accountRepositoryPort, times(1)).save(currentAccount);
    }
    
    @Test
    void testDebitAccount_CurrentAccount_ExceedsOverdraft() {
        currentAccount.setBalance(new BigDecimal("500.00"));
        when(accountRepositoryPort.findById(1L))
            .thenReturn(Optional.of(currentAccount));
        
        DebitAccountCommand command = new DebitAccountCommand(1L, new BigDecimal("2000.00"));
        
        assertThrows(InsufficientFundsException.class, () -> {
            accountUseCase.debitAccount(command);
        });
        
        verify(accountRepositoryPort, never()).save(any());
    }
    
    @Test
    void testDebitAccount_SavingsAccount_Success() {
        when(accountRepositoryPort.findById(2L))
            .thenReturn(Optional.of(savingsAccount));
        when(accountRepositoryPort.save(any(SavingsAccount.class)))
            .thenReturn(savingsAccount);
        
        DebitAccountCommand command = new DebitAccountCommand(2L, new BigDecimal("200.00"));
        accountUseCase.debitAccount(command);
        
        assertEquals(new BigDecimal("300.00"), savingsAccount.getBalance());
        verify(accountRepositoryPort, times(1)).save(savingsAccount);
    }
    
    @Test
    void testDebitAccount_SavingsAccount_InsufficientFunds() {
        when(accountRepositoryPort.findById(2L))
            .thenReturn(Optional.of(savingsAccount));
        
        DebitAccountCommand command = new DebitAccountCommand(2L, new BigDecimal("1000.00"));
        
        assertThrows(InsufficientFundsException.class, () -> {
            accountUseCase.debitAccount(command);
        });
        
        verify(accountRepositoryPort, never()).save(any());
    }
    
    @Test
    void testGetAccountById_NotFound() {
        when(accountRepositoryPort.findById(999L))
            .thenReturn(Optional.empty());
        
        assertThrows(AccountNotFoundException.class, () -> {
            accountUseCase.getAccountById(999L);
        });
    }
}

