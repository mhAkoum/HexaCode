package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.SavingsAccount;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.AccountRepository;
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
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;
    
    @BeforeEach
    void setUp() {
        currentAccount = new CurrentAccount();
        currentAccount.setId(1L);
        currentAccount.setAccountNumber("CA001");
        currentAccount.setBalance(new BigDecimal("1000.00"));
        currentAccount.setOpeningDate(LocalDate.now());
        currentAccount.setOverdraftLimit(new BigDecimal("1000.00"));
        
        savingsAccount = new SavingsAccount();
        savingsAccount.setId(2L);
        savingsAccount.setAccountNumber("SA001");
        savingsAccount.setBalance(new BigDecimal("500.00"));
        savingsAccount.setOpeningDate(LocalDate.now());
        savingsAccount.setInterestRate(new BigDecimal("3.00"));
    }
    
    @Test
    void testCredit_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.save(any())).thenReturn(currentAccount);
        
        BigDecimal creditAmount = new BigDecimal("200.00");
        accountService.credit(1L, creditAmount);
        
        assertEquals(new BigDecimal("1200.00"), currentAccount.getBalance());
        verify(accountRepository, times(1)).save(currentAccount);
    }
    
    @Test
    void testCredit_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.credit(1L, BigDecimal.ZERO);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.credit(1L, new BigDecimal("-100.00"));
        });
        
        verify(accountRepository, never()).save(any());
    }
    
    @Test
    void testDebit_CurrentAccount_WithinOverdraft() {
        currentAccount.setBalance(new BigDecimal("500.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(currentAccount));
        when(accountRepository.save(any())).thenReturn(currentAccount);
        
        BigDecimal debitAmount = new BigDecimal("1200.00");
        accountService.debit(1L, debitAmount);
        
        assertEquals(new BigDecimal("-700.00"), currentAccount.getBalance());
        verify(accountRepository, times(1)).save(currentAccount);
    }
    
    @Test
    void testDebit_CurrentAccount_ExceedsOverdraft() {
        currentAccount.setBalance(new BigDecimal("500.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(currentAccount));
        
        BigDecimal debitAmount = new BigDecimal("2000.00");
        
        assertThrows(InsufficientFundsException.class, () -> {
            accountService.debit(1L, debitAmount);
        });
        
        verify(accountRepository, never()).save(any());
    }
    
    @Test
    void testDebit_SavingsAccount_Success() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(savingsAccount));
        when(accountRepository.save(any())).thenReturn(savingsAccount);
        
        BigDecimal debitAmount = new BigDecimal("200.00");
        accountService.debit(2L, debitAmount);
        
        assertEquals(new BigDecimal("300.00"), savingsAccount.getBalance());
        verify(accountRepository, times(1)).save(savingsAccount);
    }
    
    @Test
    void testDebit_SavingsAccount_InsufficientFunds() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(savingsAccount));
        
        BigDecimal debitAmount = new BigDecimal("1000.00");
        
        assertThrows(InsufficientFundsException.class, () -> {
            accountService.debit(2L, debitAmount);
        });
        
        verify(accountRepository, never()).save(any());
    }
    
    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(999L);
        });
    }
}

