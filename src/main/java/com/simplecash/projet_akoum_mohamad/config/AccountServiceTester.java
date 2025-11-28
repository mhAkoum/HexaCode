package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.domain.Account;
import com.simplecash.projet_akoum_mohamad.domain.CurrentAccount;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import com.simplecash.projet_akoum_mohamad.repository.AccountRepository;
import com.simplecash.projet_akoum_mohamad.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Configuration
public class AccountServiceTester {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceTester.class);
    
    @Bean
    @Order(3)
    CommandLineRunner testAccountService(AccountService accountService, AccountRepository accountRepository) {
        
        return args -> {
            logger.info("=== Testing Account Service ===");
            
            Account testAccount = accountRepository.findAll().stream()
                    .filter(a -> a.getAccountNumber().equals("CA001"))
                    .findFirst()
                    .orElse(null);
            
            if (testAccount == null) {
                logger.warn("CA001 account not found, skipping account service tests");
                return;
            }
            
            logger.info("Testing with account: {} (Balance: {})", testAccount.getAccountNumber(), testAccount.getBalance());
            
            BigDecimal initialBalance = testAccount.getBalance();
            
            logger.info("Test 1: Credit 500.00");
            Account credited = accountService.credit(testAccount.getId(), new BigDecimal("500.00"));
            logger.info("Balance after credit: {}", credited.getBalance());
            
            logger.info("Test 2: Debit 200.00");
            Account debited = accountService.debit(testAccount.getId(), new BigDecimal("200.00"));
            logger.info("Balance after debit: {}", debited.getBalance());
            
            Account reloadedAccount = accountService.getAccountById(testAccount.getId());
            if (reloadedAccount instanceof CurrentAccount) {
                CurrentAccount currentAccount = (CurrentAccount) reloadedAccount;
                BigDecimal overdraftLimit = currentAccount.getOverdraftLimit();
                BigDecimal currentBalance = reloadedAccount.getBalance();
                BigDecimal maxDebit = currentBalance.add(overdraftLimit);
                
                logger.info("Test 3: Testing overdraft (limit: {}, current balance: {}, max debit: {})", 
                        overdraftLimit, currentBalance, maxDebit);
                
                logger.info("Test 3a: Debit within overdraft limit");
                try {
                    Account overdraftTest = accountService.debit(testAccount.getId(), maxDebit);
                    logger.info("Balance after overdraft debit: {}", overdraftTest.getBalance());
                } catch (InsufficientFundsException e) {
                    logger.warn("Overdraft test failed: {}", e.getMessage());
                }
                
                Account afterOverdraft = accountService.getAccountById(testAccount.getId());
                BigDecimal newBalance = afterOverdraft.getBalance();
                BigDecimal newMaxDebit = newBalance.add(overdraftLimit);
                
                logger.info("Test 3b: Try to exceed overdraft limit");
                try {
                    accountService.debit(testAccount.getId(), newMaxDebit.add(new BigDecimal("1.00")));
                    logger.warn("ERROR: Should have thrown InsufficientFundsException!");
                } catch (InsufficientFundsException e) {
                    logger.info("SUCCESS: InsufficientFundsException thrown correctly: {}", e.getMessage());
                }
            }
            
            logger.info("Test 4: Try to debit negative amount");
            try {
                accountService.debit(testAccount.getId(), new BigDecimal("-100.00"));
                logger.warn("ERROR: Should have thrown IllegalArgumentException!");
            } catch (IllegalArgumentException e) {
                logger.info("SUCCESS: IllegalArgumentException thrown correctly: {}", e.getMessage());
            }
            
            logger.info("Test 5: Try to credit negative amount");
            try {
                accountService.credit(testAccount.getId(), new BigDecimal("-50.00"));
                logger.warn("ERROR: Should have thrown IllegalArgumentException!");
            } catch (IllegalArgumentException e) {
                logger.info("SUCCESS: IllegalArgumentException thrown correctly: {}", e.getMessage());
            }
            
            logger.info("=== Account Service Tests Completed ===");
        };
    }
}

