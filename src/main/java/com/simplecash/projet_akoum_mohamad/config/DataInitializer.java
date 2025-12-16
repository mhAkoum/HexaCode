package com.simplecash.projet_akoum_mohamad.config;

import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.entity.*;
import com.simplecash.projet_akoum_mohamad.adapter.out.persistence.repository.*;
import com.simplecash.projet_akoum_mohamad.domain.model.CardStatus;
import com.simplecash.projet_akoum_mohamad.domain.model.CardType;
import com.simplecash.projet_akoum_mohamad.domain.model.ClientType;
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
            
            AgencyEntity theAgency = new AgencyEntity("AG001", LocalDate.now());
            agencyRepository.save(theAgency);
            logger.info("Created agency: {}", theAgency.getCode());
            
            ManagerEntity kimo = new ManagerEntity("Kimo the Manager", "kimo@simplecash.com");
            kimo.setAgency(theAgency);
            theAgency.setManager(kimo);
            managerRepository.save(kimo);
            agencyRepository.save(theAgency);
            logger.info("Created manager: {} for agency: {}", kimo.getName(), theAgency.getCode());
            
            AdvisorEntity tchoupi = new AdvisorEntity("Tchoupi", "tchoupi@simplecash.com");
            tchoupi.setAgency(theAgency);
            theAgency.addAdvisor(tchoupi);
            advisorRepository.save(tchoupi);
            logger.info("Created advisor: {} for agency: {}", tchoupi.getName(), theAgency.getCode());
            
            AdvisorEntity dora = new AdvisorEntity("Dora the Explora", "dora@simplecash.com");
            dora.setAgency(theAgency);
            theAgency.addAdvisor(dora);
            advisorRepository.save(dora);
            logger.info("Created advisor: {} for agency: {}", dora.getName(), theAgency.getCode());
            
            ClientEntity john = new ClientEntity("John Sina", "Crater Base Alpha, Mars", "111-222-3333", "john.sina@mars.com", ClientType.PRIVATE);
            john.setAdvisor(tchoupi);
            tchoupi.addClient(john);
            clientRepository.save(john);
            logger.info("Created client: {} for advisor: {}", john.getName(), tchoupi.getName());
            
            ClientEntity epita = new ClientEntity("EPITA Students", "14-16 Rue Voltaire, Kremlin-Bicêtre", "01-44-08-00-00", "students@epita.fr", ClientType.BUSINESS);
            epita.setAdvisor(tchoupi);
            tchoupi.addClient(epita);
            clientRepository.save(epita);
            logger.info("Created client: {} for advisor: {}", epita.getName(), tchoupi.getName());
            
            ClientEntity marsGuy = new ClientEntity("Mars Explorer", "Red Planet Colony 5, Mars", "999-888-7777", "explorer@mars.com", ClientType.PRIVATE);
            marsGuy.setAdvisor(dora);
            dora.addClient(marsGuy);
            clientRepository.save(marsGuy);
            logger.info("Created client: {} for advisor: {}", marsGuy.getName(), dora.getName());
            
            CurrentAccountEntity johnCurrent = new CurrentAccountEntity("CA001", BigDecimal.ZERO, LocalDate.now());
            johnCurrent.setClient(john);
            john.setCurrentAccount(johnCurrent);
            accountRepository.save(johnCurrent);
            logger.info("Created current account {} for client {} with overdraft limit: {}", 
                    johnCurrent.getAccountNumber(), john.getName(), johnCurrent.getOverdraftLimit());
            
            SavingsAccountEntity johnSavings = new SavingsAccountEntity("SA001", BigDecimal.ZERO, LocalDate.now());
            johnSavings.setClient(john);
            john.setSavingsAccount(johnSavings);
            accountRepository.save(johnSavings);
            logger.info("Created savings account {} for client {} with interest rate: {}%", 
                    johnSavings.getAccountNumber(), john.getName(), johnSavings.getInterestRate());
            
            CurrentAccountEntity epitaCurrent = new CurrentAccountEntity("CA002", new BigDecimal("5000.00"), LocalDate.now());
            epitaCurrent.setClient(epita);
            epita.setCurrentAccount(epitaCurrent);
            accountRepository.save(epitaCurrent);
            logger.info("Created current account {} for client {} with overdraft limit: {}", 
                    epitaCurrent.getAccountNumber(), epita.getName(), epitaCurrent.getOverdraftLimit());
            
            CardEntity johnCard1 = new CardEntity(CardType.VISA_PREMIER, CardStatus.ACTIVE);
            johnCard1.setClient(john);
            john.addCard(johnCard1);
            cardRepository.save(johnCard1);
            logger.info("Created card {} for client {}", johnCard1.getCardType(), john.getName());
            
            CardEntity johnCard2 = new CardEntity(CardType.VISA_ELECTRON, CardStatus.ACTIVE);
            johnCard2.setClient(john);
            john.addCard(johnCard2);
            cardRepository.save(johnCard2);
            logger.info("Created card {} for client {}", johnCard2.getCardType(), john.getName());
            
            CardEntity epitaCard = new CardEntity(CardType.VISA_PREMIER, CardStatus.ACTIVE);
            epitaCard.setClient(epita);
            epita.addCard(epitaCard);
            cardRepository.save(epitaCard);
            logger.info("Created card {} for client {}", epitaCard.getCardType(), epita.getName());
            
            logger.info("Test data initialization completed!");
            logger.info("Agency: {} with {} advisors and {} total clients", 
                    theAgency.getCode(), 
                    theAgency.getAdvisors().size(),
                    theAgency.getAdvisors().stream()
                            .mapToInt(advisor -> advisor.getClients().size())
                            .sum());
        };
    }
}
