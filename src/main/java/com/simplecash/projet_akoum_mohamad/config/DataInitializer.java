package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.*;
import com.simplecash.projet_akoum_mohamad.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Bean
    @Order(1)
    CommandLineRunner initDatabase(
            AgencyJpaRepository agencyRepository,
            ManagerJpaRepository managerRepository,
            AdvisorJpaRepository advisorRepository,
            ClientJpaRepository clientRepository,
            AccountJpaRepository accountRepository,
            CardJpaRepository cardRepository) {
        
        return args -> {
            logger.info("Initializing test data...");
            
            AgencyEntity theAgency = new AgencyEntity();
            theAgency.setCode("AG001");
            theAgency.setCreationDate(LocalDate.now());
            theAgency = agencyRepository.save(theAgency);
            logger.info("Created agency: {}", theAgency.getCode());
            
            ManagerEntity kimo = new ManagerEntity();
            kimo.setName("Kimo the Manager");
            kimo.setEmail("kimo@simplecash.com");
            kimo.setAgency(theAgency);
            theAgency.setManager(kimo);
            kimo = managerRepository.save(kimo);
            theAgency = agencyRepository.save(theAgency);
            logger.info("Created manager: {} for agency: {}", kimo.getName(), theAgency.getCode());
            
            AdvisorEntity tchoupi = new AdvisorEntity();
            tchoupi.setName("Tchoupi");
            tchoupi.setEmail("tchoupi@simplecash.com");
            tchoupi.setAgency(theAgency);
            tchoupi = advisorRepository.save(tchoupi);
            logger.info("Created advisor: {} for agency: {}", tchoupi.getName(), theAgency.getCode());
            
            AdvisorEntity dora = new AdvisorEntity();
            dora.setName("Dora the Explora");
            dora.setEmail("dora@simplecash.com");
            dora.setAgency(theAgency);
            dora = advisorRepository.save(dora);
            logger.info("Created advisor: {} for agency: {}", dora.getName(), theAgency.getCode());
            
            ClientEntity john = new ClientEntity();
            john.setName("John Sina");
            john.setAddress("Crater Base Alpha, Mars");
            john.setPhone("111-222-3333");
            john.setEmail("john.sina@mars.com");
            john.setClientType(ClientType.PRIVATE);
            john.setAdvisor(tchoupi);
            john = clientRepository.save(john);
            logger.info("Created client: {} for advisor: {}", john.getName(), tchoupi.getName());
            
            ClientEntity epita = new ClientEntity();
            epita.setName("EPITA Students");
            epita.setAddress("14-16 Rue Voltaire, Kremlin-Bicêtre");
            epita.setPhone("01-44-08-00-00");
            epita.setEmail("students@epita.fr");
            epita.setClientType(ClientType.BUSINESS);
            epita.setAdvisor(tchoupi);
            epita = clientRepository.save(epita);
            logger.info("Created client: {} for advisor: {}", epita.getName(), tchoupi.getName());
            
            ClientEntity marsGuy = new ClientEntity();
            marsGuy.setName("Mars Explorer");
            marsGuy.setAddress("Red Planet Colony 5, Mars");
            marsGuy.setPhone("999-888-7777");
            marsGuy.setEmail("explorer@mars.com");
            marsGuy.setClientType(ClientType.PRIVATE);
            marsGuy.setAdvisor(dora);
            marsGuy = clientRepository.save(marsGuy);
            logger.info("Created client: {} for advisor: {}", marsGuy.getName(), dora.getName());
            
            CurrentAccountEntity johnCurrent = new CurrentAccountEntity();
            johnCurrent.setAccountNumber("CA001");
            johnCurrent.setBalance(BigDecimal.ZERO);
            johnCurrent.setOpeningDate(LocalDate.now());
            johnCurrent.setClient(john);
            john.setCurrentAccount(johnCurrent);
            johnCurrent = (CurrentAccountEntity) accountRepository.save(johnCurrent);
            john = clientRepository.save(john);
            logger.info("Created current account {} for client {} with overdraft limit: {}", 
                    johnCurrent.getAccountNumber(), john.getName(), johnCurrent.getOverdraftLimit());
            
            SavingsAccountEntity johnSavings = new SavingsAccountEntity();
            johnSavings.setAccountNumber("SA001");
            johnSavings.setBalance(BigDecimal.ZERO);
            johnSavings.setOpeningDate(LocalDate.now());
            johnSavings.setClient(john);
            john.setSavingsAccount(johnSavings);
            johnSavings = (SavingsAccountEntity) accountRepository.save(johnSavings);
            john = clientRepository.save(john);
            logger.info("Created savings account {} for client {} with interest rate: {}%", 
                    johnSavings.getAccountNumber(), john.getName(), johnSavings.getInterestRate());
            
            CurrentAccountEntity epitaCurrent = new CurrentAccountEntity();
            epitaCurrent.setAccountNumber("CA002");
            epitaCurrent.setBalance(new BigDecimal("5000.00"));
            epitaCurrent.setOpeningDate(LocalDate.now());
            epitaCurrent.setClient(epita);
            epita.setCurrentAccount(epitaCurrent);
            epitaCurrent = (CurrentAccountEntity) accountRepository.save(epitaCurrent);
            epita = clientRepository.save(epita);
            logger.info("Created current account {} for client {} with overdraft limit: {}", 
                    epitaCurrent.getAccountNumber(), epita.getName(), epitaCurrent.getOverdraftLimit());
            
            CardEntity johnCard1 = new CardEntity();
            johnCard1.setCardType(CardType.VISA_PREMIER);
            johnCard1.setStatus(CardStatus.ACTIVE);
            johnCard1.setClient(john);
            johnCard1 = cardRepository.save(johnCard1);
            logger.info("Created card {} for client {}", johnCard1.getCardType(), john.getName());
            
            CardEntity johnCard2 = new CardEntity();
            johnCard2.setCardType(CardType.VISA_ELECTRON);
            johnCard2.setStatus(CardStatus.ACTIVE);
            johnCard2.setClient(john);
            johnCard2 = cardRepository.save(johnCard2);
            logger.info("Created card {} for client {}", johnCard2.getCardType(), john.getName());
            
            CardEntity epitaCard = new CardEntity();
            epitaCard.setCardType(CardType.VISA_PREMIER);
            epitaCard.setStatus(CardStatus.ACTIVE);
            epitaCard.setClient(epita);
            epitaCard = cardRepository.save(epitaCard);
            logger.info("Created card {} for client {}", epitaCard.getCardType(), epita.getName());
            
            logger.info("Test data initialization completed!");
            logger.info("Agency: {} with {} advisors", 
                    theAgency.getCode(), 
                    advisorRepository.findByAgencyId(theAgency.getId()).size());
        };
    }
}

