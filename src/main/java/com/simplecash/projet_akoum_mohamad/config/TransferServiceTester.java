package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.AccountRepository;
import com.simplecash.projet_akoum_mohamad.service.AccountService;
import com.simplecash.projet_akoum_mohamad.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Configuration
public class TransferServiceTester {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceTester.class);
    
    @Bean
    @Order(4)
    CommandLineRunner testTransferService(
            TransferService transferService,
            AccountService accountService,
            AccountRepository accountRepository) {
        
        return args -> {
            logger.info("=== Testing Transfer Service ===");
            
            Account sourceAccount = accountRepository.findAll().stream()
                    .filter(a -> a.getAccountNumber().equals("CA001"))
                    .findFirst()
                    .orElse(null);
            
            Account targetAccount = accountRepository.findAll().stream()
                    .filter(a -> a.getAccountNumber().equals("CA002"))
                    .findFirst()
                    .orElse(null);
            
            if (sourceAccount == null || targetAccount == null) {
                logger.warn("Required accounts not found, skipping transfer tests");
                return;
            }
            
            BigDecimal sourceInitialBalance = sourceAccount.getBalance();
            BigDecimal targetInitialBalance = targetAccount.getBalance();
            
            logger.info("Source account: {} (Balance: {})", sourceAccount.getAccountNumber(), sourceInitialBalance);
            logger.info("Target account: {} (Balance: {})", targetAccount.getAccountNumber(), targetInitialBalance);
            
            logger.info("Test 1: Successful transfer of 100.00");
            try {
                transferService.transfer(sourceAccount.getId(), targetAccount.getId(), new BigDecimal("100.00"));
                
                Account updatedSource = accountService.getAccountById(sourceAccount.getId());
                Account updatedTarget = accountService.getAccountById(targetAccount.getId());
                
                logger.info("Source balance after transfer: {} (expected: {})", 
                        updatedSource.getBalance(), 
                        sourceInitialBalance.subtract(new BigDecimal("100.00")));
                logger.info("Target balance after transfer: {} (expected: {})", 
                        updatedTarget.getBalance(), 
                        targetInitialBalance.add(new BigDecimal("100.00")));
                
                if (updatedSource.getBalance().equals(sourceInitialBalance.subtract(new BigDecimal("100.00"))) &&
                    updatedTarget.getBalance().equals(targetInitialBalance.add(new BigDecimal("100.00")))) {
                    logger.info("SUCCESS: Transfer completed correctly");
                } else {
                    logger.warn("ERROR: Balances don't match expected values");
                }
                
            } catch (Exception e) {
                logger.error("Transfer failed: {}", e.getMessage());
            }
            
            Account beforeFailedTest = accountService.getAccountById(sourceAccount.getId());
            Account targetBeforeFailedTest = accountService.getAccountById(targetAccount.getId());
            BigDecimal balanceBeforeFailed = beforeFailedTest.getBalance();
            BigDecimal targetBalanceBeforeFailed = targetBeforeFailedTest.getBalance();
            
            logger.info("Test 2: Failed transfer (insufficient funds)");
            logger.info("Source balance before failed transfer: {}", balanceBeforeFailed);
            logger.info("Target balance before failed transfer: {}", targetBalanceBeforeFailed);
            
            try {
                BigDecimal hugeAmount = balanceBeforeFailed.add(new BigDecimal("10000.00"));
                transferService.transfer(sourceAccount.getId(), targetAccount.getId(), hugeAmount);
                logger.warn("ERROR: Should have thrown InsufficientFundsException!");
            } catch (InsufficientFundsException e) {
                logger.info("SUCCESS: InsufficientFundsException thrown correctly");
                
                Account afterFailedTest = accountService.getAccountById(sourceAccount.getId());
                Account targetAfterFailedTest = accountService.getAccountById(targetAccount.getId());
                
                logger.info("Source balance after failed transfer: {} (should be unchanged: {})", 
                        afterFailedTest.getBalance(), 
                        balanceBeforeFailed);
                logger.info("Target balance after failed transfer: {} (should be unchanged: {})", 
                        targetAfterFailedTest.getBalance(), 
                        targetBalanceBeforeFailed);
                
                if (afterFailedTest.getBalance().equals(balanceBeforeFailed) &&
                    targetAfterFailedTest.getBalance().equals(targetBalanceBeforeFailed)) {
                    logger.info("SUCCESS: Transaction rolled back - both accounts unchanged");
                } else {
                    logger.warn("ERROR: Transaction did not roll back correctly!");
                }
            } catch (Exception e) {
                logger.error("Unexpected exception: {}", e.getMessage());
            }
            
            logger.info("Test 3: Try to transfer to same account");
            try {
                transferService.transfer(sourceAccount.getId(), sourceAccount.getId(), new BigDecimal("50.00"));
                logger.warn("ERROR: Should have thrown IllegalArgumentException!");
            } catch (IllegalArgumentException e) {
                logger.info("SUCCESS: IllegalArgumentException thrown correctly: {}", e.getMessage());
            }
            
            logger.info("=== Transfer Service Tests Completed ===");
        };
    }
}

