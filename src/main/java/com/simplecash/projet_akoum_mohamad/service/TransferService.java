package com.simplecash.projet_akoum_mohamad.service;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.Transfer;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class TransferService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
    
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    
    @Autowired
    public TransferService(AccountService accountService, TransferRepository transferRepository) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
    }
    
    public Transfer transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0");
        }
        
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts cannot be the same");
        }
        
        logger.info("TRANSFER;{};{};{};{};STARTED", 
                LocalDateTime.now(), 
                sourceAccountId, 
                targetAccountId, 
                amount);
        
        try {
            Account sourceAccount = accountService.getAccountById(sourceAccountId);
            Account targetAccount = accountService.getAccountById(targetAccountId);
            
            accountService.debit(sourceAccountId, amount);
            accountService.credit(targetAccountId, amount);
            
            Transfer transfer = new Transfer(
                    sourceAccount,
                    targetAccount,
                    amount,
                    LocalDateTime.now(),
                    "SUCCESS"
            );
            transfer = transferRepository.save(transfer);
            
            logger.info("TRANSFER;{};{};{};{};SUCCESS", 
                    LocalDateTime.now(), 
                    sourceAccountId, 
                    targetAccountId, 
                    amount);
            
            return transfer;
            
        } catch (AccountNotFoundException e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Account not found: {}", 
                    LocalDateTime.now(), 
                    sourceAccountId, 
                    targetAccountId, 
                    amount,
                    e.getMessage());
            throw e;
            
        } catch (InsufficientFundsException e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Insufficient funds: {}", 
                    LocalDateTime.now(), 
                    sourceAccountId, 
                    targetAccountId, 
                    amount,
                    e.getMessage());
            throw e;
            
        } catch (Exception e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Unexpected error: {}", 
                    LocalDateTime.now(), 
                    sourceAccountId, 
                    targetAccountId, 
                    amount,
                    e.getMessage());
            throw e;
        }
    }
}

