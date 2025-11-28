package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.domain.Transfer;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private TransferRepository transferRepository;
    
    @InjectMocks
    private TransferService transferService;
    
    private CurrentAccount sourceAccount;
    private CurrentAccount targetAccount;
    
    @BeforeEach
    void setUp() {
        sourceAccount = new CurrentAccount();
        sourceAccount.setId(1L);
        sourceAccount.setAccountNumber("CA001");
        sourceAccount.setBalance(new BigDecimal("1000.00"));
        sourceAccount.setOpeningDate(LocalDate.now());
        sourceAccount.setOverdraftLimit(new BigDecimal("1000.00"));
        
        targetAccount = new CurrentAccount();
        targetAccount.setId(2L);
        targetAccount.setAccountNumber("CA002");
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setOpeningDate(LocalDate.now());
        targetAccount.setOverdraftLimit(new BigDecimal("1000.00"));
    }
    
    @Test
    void testTransfer_Success() {
        BigDecimal transferAmount = new BigDecimal("200.00");
        
        when(accountService.getAccountById(1L)).thenReturn(sourceAccount);
        when(accountService.getAccountById(2L)).thenReturn(targetAccount);
        when(accountService.debit(1L, transferAmount)).thenReturn(sourceAccount);
        when(accountService.credit(2L, transferAmount)).thenReturn(targetAccount);
        when(transferRepository.save(any(Transfer.class))).thenAnswer(invocation -> {
            Transfer transfer = invocation.getArgument(0);
            transfer.setId(1L);
            return transfer;
        });
        
        Transfer result = transferService.transfer(1L, 2L, transferAmount);
        
        assertNotNull(result);
        assertEquals(transferAmount, result.getAmount());
        verify(accountService, times(1)).debit(1L, transferAmount);
        verify(accountService, times(1)).credit(2L, transferAmount);
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }
    
    @Test
    void testTransfer_SameAccount() {
        BigDecimal transferAmount = new BigDecimal("200.00");
        
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer(1L, 1L, transferAmount);
        });
        
        verify(accountService, never()).debit(anyLong(), any(BigDecimal.class));
        verify(accountService, never()).credit(anyLong(), any(BigDecimal.class));
        verify(transferRepository, never()).save(any());
    }
    
    @Test
    void testTransfer_InsufficientFunds_Rollback() {
        BigDecimal transferAmount = new BigDecimal("5000.00");
        
        when(accountService.getAccountById(1L)).thenReturn(sourceAccount);
        when(accountService.getAccountById(2L)).thenReturn(targetAccount);
        when(accountService.debit(1L, transferAmount))
                .thenThrow(new InsufficientFundsException("CA001", 
                        sourceAccount.getBalance(), 
                        transferAmount, 
                        sourceAccount.getOverdraftLimit()));
        
        assertThrows(InsufficientFundsException.class, () -> {
            transferService.transfer(1L, 2L, transferAmount);
        });
        
        verify(accountService, times(1)).debit(1L, transferAmount);
        verify(accountService, never()).credit(anyLong(), any(BigDecimal.class));
        verify(transferRepository, never()).save(any());
    }
    
    @Test
    void testTransfer_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer(1L, 2L, BigDecimal.ZERO);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer(1L, 2L, new BigDecimal("-100.00"));
        });
        
        verify(accountService, never()).debit(anyLong(), any(BigDecimal.class));
        verify(accountService, never()).credit(anyLong(), any(BigDecimal.class));
    }
}

