package com.simplecash.projet_akoum_mohamad.application.usecase;

import com.simplecash.projet_akoum_mohamad.application.dto.TransferCommand;
import com.simplecash.projet_akoum_mohamad.application.port.in.AccountUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.in.TransferUseCase;
import com.simplecash.projet_akoum_mohamad.application.port.out.TransferRepositoryPort;
import com.simplecash.projet_akoum_mohamad.domain.model.Account;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;
import com.simplecash.projet_akoum_mohamad.exception.AccountNotFoundException;
import com.simplecash.projet_akoum_mohamad.exception.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TransferUseCaseImpl implements TransferUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferUseCaseImpl.class);
    
    private final AccountUseCase accountUseCase;
    private final TransferRepositoryPort transferRepository;
    
    public TransferUseCaseImpl(AccountUseCase accountUseCase, TransferRepositoryPort transferRepository) {
        this.accountUseCase = accountUseCase;
        this.transferRepository = transferRepository;
    }
    
    @Override
    public Transfer transfer(TransferCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0");
        }
        
        if (command.getSourceAccountId().equals(command.getTargetAccountId())) {
            throw new IllegalArgumentException("Source and target accounts cannot be the same");
        }
        
        logger.info("TRANSFER;{};{};{};{};STARTED", 
                LocalDateTime.now(), 
                command.getSourceAccountId(), 
                command.getTargetAccountId(), 
                command.getAmount());
        
        try {
            Account sourceAccount = accountUseCase.getAccountById(command.getSourceAccountId());
            Account targetAccount = accountUseCase.getAccountById(command.getTargetAccountId());
            
            // Debit source account
            accountUseCase.debitAccount(
                new com.simplecash.projet_akoum_mohamad.application.dto.DebitAccountCommand(
                    command.getSourceAccountId(),
                    command.getAmount()
                )
            );
            
            // Credit target account
            accountUseCase.creditAccount(
                new com.simplecash.projet_akoum_mohamad.application.dto.CreditAccountCommand(
                    command.getTargetAccountId(),
                    command.getAmount()
                )
            );
            
            Transfer transfer = new Transfer(
                    sourceAccount,
                    targetAccount,
                    command.getAmount(),
                    LocalDateTime.now(),
                    "SUCCESS"
            );
            
            Transfer saved = transferRepository.save(transfer);
            
            logger.info("TRANSFER;{};{};{};{};SUCCESS", 
                    LocalDateTime.now(), 
                    command.getSourceAccountId(), 
                    command.getTargetAccountId(), 
                    command.getAmount());
            
            return saved;
            
        } catch (AccountNotFoundException e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Account not found: {}", 
                    LocalDateTime.now(), 
                    command.getSourceAccountId(), 
                    command.getTargetAccountId(), 
                    command.getAmount(),
                    e.getMessage());
            throw e;
            
        } catch (InsufficientFundsException e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Insufficient funds: {}", 
                    LocalDateTime.now(), 
                    command.getSourceAccountId(), 
                    command.getTargetAccountId(), 
                    command.getAmount(),
                    e.getMessage());
            throw e;
            
        } catch (Exception e) {
            logger.error("TRANSFER;{};{};{};{};FAILED;Unexpected error: {}", 
                    LocalDateTime.now(), 
                    command.getSourceAccountId(), 
                    command.getTargetAccountId(), 
                    command.getAmount(),
                    e.getMessage());
            throw e;
        }
    }
}

