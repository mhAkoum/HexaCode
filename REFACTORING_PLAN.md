# Hexagonal Architecture Refactoring Plan

## 1. Audit Summary

### Current Architecture
- **Package Structure**: Traditional layered architecture
  - `domain/`: JPA entities with `@Entity` annotations
  - `service/`: Business logic with Spring `@Service` annotations
  - `repository/`: Spring Data JPA repositories
  - `web/`: REST controllers
  - `dto/`: REST request/response DTOs
  - `exception/`: Custom exceptions and global handler

### Controllers (3)
1. **ClientController** (`/clients`)
   - GET `/clients` - Get all clients
   - GET `/clients/{id}` - Get client by ID
   - POST `/clients` - Create client
   - PUT `/clients/{id}` - Update client
   - DELETE `/clients/{id}` - Delete client

2. **AccountController** (`/accounts`)
   - GET `/accounts/{id}` - Get account by ID
   - POST `/accounts/{id}/credit` - Credit account
   - POST `/accounts/{id}/debit` - Debit account

3. **AuditController** (`/audit`)
   - GET `/audit/accounts` - Audit all accounts

### Services (5)
1. **ClientService**: Client CRUD, max clients per advisor (10), card deactivation on delete
2. **AccountService**: Account operations (credit/debit), overdraft validation
3. **TransferService**: Internal transfers, transactional, logging
4. **AuditService**: Account auditing with threshold rules
5. **AdvisorService**: Advisor queries

### Repositories (9)
- ClientRepository, AccountRepository, AdvisorRepository, AgencyRepository
- ManagerRepository, CardRepository, TransferRepository
- CurrentAccountRepository, SavingsAccountRepository

### Domain Entities (12)
- Client, Account (abstract), CurrentAccount, SavingsAccount
- Advisor, Agency, Manager
- Card, Transfer
- Enums: ClientType, CardType, CardStatus

### Business Rules Identified
1. **Client Management**:
   - Max 10 clients per advisor
   - Delete client only if all accounts have zero balance
   - Deactivate all cards when deleting client

2. **Account Operations**:
   - Credit amount must be > 0
   - CurrentAccount: can go negative up to overdraft limit (default 1000€)
   - SavingsAccount: cannot go negative
   - Debit amount must be > 0

3. **Transfers**:
   - Amount must be > 0
   - Cannot transfer to same account
   - Transactional (all-or-nothing)

4. **Audit**:
   - Private clients: threshold -5000€
   - Business clients: threshold -50000€

### Dependencies to Remove from Core
- Spring annotations: `@Service`, `@Component`, `@Repository`, `@RestController`
- JPA annotations: `@Entity`, `@Table`, `@Id`, `@Column`, etc.
- Spring packages: `org.springframework.*`
- JPA packages: `jakarta.persistence.*`, `org.hibernate.*`
- Web packages: `jakarta.servlet.*`, `org.springframework.web.*`

---

## 2. Target Package Structure

```
com.simplecash.projet_akoum_mohamad/
├── domain/                          # Pure domain (NO Spring/JPA)
│   ├── model/
│   │   ├── Client.java
│   │   ├── Account.java
│   │   ├── CurrentAccount.java
│   │   ├── SavingsAccount.java
│   │   ├── Advisor.java
│   │   ├── Agency.java
│   │   ├── Manager.java
│   │   ├── Card.java
│   │   ├── Transfer.java
│   │   └── (enums: ClientType, CardType, CardStatus)
│   └── service/                     # Pure domain services (if needed)
│
├── application/                     # Application layer (NO Spring/JPA)
│   ├── port.in/                     # Use case interfaces (inbound)
│   │   ├── ClientUseCase.java
│   │   ├── AccountUseCase.java
│   │   ├── TransferUseCase.java
│   │   └── AuditUseCase.java
│   ├── port.out/                    # Repository/Gateway interfaces (outbound)
│   │   ├── ClientRepositoryPort.java
│   │   ├── AccountRepositoryPort.java
│   │   ├── AdvisorRepositoryPort.java
│   │   ├── CardRepositoryPort.java
│   │   └── TransferRepositoryPort.java
│   ├── usecase/                     # Use case implementations
│   │   ├── ClientUseCaseImpl.java
│   │   ├── AccountUseCaseImpl.java
│   │   ├── TransferUseCaseImpl.java
│   │   └── AuditUseCaseImpl.java
│   ├── dto/                         # Application DTOs (commands/queries)
│   │   ├── CreateClientCommand.java
│   │   ├── UpdateClientCommand.java
│   │   ├── CreditAccountCommand.java
│   │   ├── DebitAccountCommand.java
│   │   └── TransferCommand.java
│   └── mapper/                      # Domain <-> Application mapping
│
├── adapter.in.web/                  # Web adapters (Spring)
│   ├── controller/
│   │   ├── ClientController.java
│   │   ├── AccountController.java
│   │   └── AuditController.java
│   ├── dto/                         # REST DTOs
│   │   ├── ClientDTO.java
│   │   ├── AccountDTO.java
│   │   ├── CreateClientRequest.java
│   │   ├── UpdateClientRequest.java
│   │   ├── CreditRequest.java
│   │   ├── DebitRequest.java
│   │   └── AuditReportDTO.java
│   ├── mapper/                      # REST <-> Application mapping
│   │   ├── ClientWebMapper.java
│   │   ├── AccountWebMapper.java
│   │   └── AuditWebMapper.java
│   └── exception/
│       └── GlobalExceptionHandler.java
│
├── adapter.out.persistence/         # Persistence adapters (Spring + JPA)
│   ├── entity/                      # JPA entities
│   │   ├── ClientEntity.java
│   │   ├── AccountEntity.java
│   │   ├── CurrentAccountEntity.java
│   │   ├── SavingsAccountEntity.java
│   │   ├── AdvisorEntity.java
│   │   ├── AgencyEntity.java
│   │   ├── ManagerEntity.java
│   │   ├── CardEntity.java
│   │   └── TransferEntity.java
│   ├── repository/                  # Spring Data repositories
│   │   ├── ClientJpaRepository.java
│   │   ├── AccountJpaRepository.java
│   │   ├── AdvisorJpaRepository.java
│   │   ├── CardJpaRepository.java
│   │   └── TransferJpaRepository.java
│   ├── adapter/                     # Implements port.out
│   │   ├── ClientPersistenceAdapter.java
│   │   ├── AccountPersistenceAdapter.java
│   │   ├── AdvisorPersistenceAdapter.java
│   │   ├── CardPersistenceAdapter.java
│   │   └── TransferPersistenceAdapter.java
│   └── mapper/                      # Domain <-> JPA mapping
│       ├── ClientPersistenceMapper.java
│       ├── AccountPersistenceMapper.java
│       ├── AdvisorPersistenceMapper.java
│       ├── CardPersistenceMapper.java
│       └── TransferPersistenceMapper.java
│
├── adapter.out.logging/             # Logging adapter (if needed)
│
└── config/                          # Spring configuration
    ├── ApplicationConfig.java        # Wire use cases with adapters
    └── DataInitializer.java          # Keep for test data
```

---

## 3. Ports Definition

### Port.In (Use Case Interfaces)

1. **ClientUseCase**
   - `List<Client> getAllClients()`
   - `Client getClientById(Long id)`
   - `Client createClient(CreateClientCommand command)`
   - `Client updateClient(Long id, UpdateClientCommand command)`
   - `void deleteClient(Long id)`

2. **AccountUseCase**
   - `Account getAccountById(Long id)`
   - `Account getAccountByNumber(String accountNumber)`
   - `Account creditAccount(CreditAccountCommand command)`
   - `Account debitAccount(DebitAccountCommand command)`

3. **TransferUseCase**
   - `Transfer transfer(TransferCommand command)`

4. **AuditUseCase**
   - `AuditReport auditAllAccounts()`

### Port.Out (Repository Interfaces)

1. **ClientRepositoryPort**
   - `Client save(Client client)`
   - `Optional<Client> findById(Long id)`
   - `List<Client> findAll()`
   - `List<Client> findByAdvisorId(Long advisorId)`
   - `void delete(Client client)`

2. **AccountRepositoryPort**
   - `Account save(Account account)`
   - `Optional<Account> findById(Long id)`
   - `Optional<Account> findByAccountNumber(String accountNumber)`

3. **AdvisorRepositoryPort**
   - `Optional<Advisor> findById(Long id)`
   - `List<Advisor> findAll()`

4. **CardRepositoryPort**
   - `Card save(Card card)`
   - `List<Card> findByClientId(Long clientId)`

5. **TransferRepositoryPort**
   - `Transfer save(Transfer transfer)`

---

## 4. Step-by-Step Refactoring Plan

### Phase 1: Create Core Domain (Pure Java)
1. Create `domain.model` package
2. Copy domain entities, remove all JPA annotations
3. Keep business logic in domain models (constructors, validation methods)
4. Move enums to domain.model

### Phase 2: Create Application Layer
1. Create `application.port.in` interfaces
2. Create `application.port.out` interfaces
3. Create `application.dto` (commands/queries)
4. Create `application.usecase` implementations
5. Move business logic from services to use cases

### Phase 3: Create Persistence Adapters
1. Create `adapter.out.persistence.entity` (JPA entities)
2. Create `adapter.out.persistence.repository` (Spring Data repos)
3. Create `adapter.out.persistence.mapper` (domain <-> JPA)
4. Create `adapter.out.persistence.adapter` (implements port.out)

### Phase 4: Create Web Adapters
1. Move controllers to `adapter.in.web.controller`
2. Keep REST DTOs in `adapter.in.web.dto`
3. Create `adapter.in.web.mapper` (REST <-> Application)
4. Update controllers to call use cases via port.in
5. Update exception handler

### Phase 5: Wiring & Configuration
1. Create `config.ApplicationConfig` with `@Bean` methods
2. Wire use cases with adapters
3. Update main application class

### Phase 6: Testing & Verification
1. Verify core compiles without Spring/JPA
2. Run existing tests
3. Update tests if needed
4. Verify all endpoints work

---

## 5. File-by-File Refactoring Notes

### Domain Models (domain.model)
- **Client.java**: Remove `@Entity`, `@Table`, `@Id`, `@Column`, `@ManyToOne`, `@OneToOne`, `@OneToMany`
- **Account.java**: Remove JPA annotations, keep abstract class
- **CurrentAccount.java**: Remove JPA annotations
- **SavingsAccount.java**: Remove JPA annotations
- **Advisor.java**: Remove JPA annotations
- **Transfer.java**: Remove JPA annotations
- **Card.java**: Remove JPA annotations
- **Agency.java**: Remove JPA annotations
- **Manager.java**: Remove JPA annotations
- **Enums**: Keep as-is (no changes needed)

### Services → Use Cases
- **ClientService** → `ClientUseCaseImpl` (implements `ClientUseCase`)
- **AccountService** → `AccountUseCaseImpl` (implements `AccountUseCase`)
- **TransferService** → `TransferUseCaseImpl` (implements `TransferUseCase`)
- **AuditService** → `AuditUseCaseImpl` (implements `AuditUseCase`)
- **AdvisorService** → Logic moved to `ClientUseCaseImpl` or separate `AdvisorUseCase`

### Repositories → Ports + Adapters
- **ClientRepository** → `ClientRepositoryPort` (interface) + `ClientPersistenceAdapter` (implementation)
- **AccountRepository** → `AccountRepositoryPort` + `AccountPersistenceAdapter`
- Similar for other repositories

### Controllers → Web Adapters
- **ClientController** → `adapter.in.web.controller.ClientController` (calls `ClientUseCase`)
- **AccountController** → `adapter.in.web.controller.AccountController` (calls `AccountUseCase`)
- **AuditController** → `adapter.in.web.controller.AuditController` (calls `AuditUseCase`)

### DTOs
- REST DTOs stay in `adapter.in.web.dto`
- Create application DTOs in `application.dto` for use cases

---

## 6. Quality Gates Checklist

- [ ] No Spring imports in `domain` package
- [ ] No Spring imports in `application` package
- [ ] No JPA annotations in domain models
- [ ] Controllers don't depend on repositories directly
- [ ] All use cases are testable with mocked ports
- [ ] Core modules compile without Spring/JPA on classpath
- [ ] All existing endpoints work
- [ ] All existing tests pass
- [ ] Business rules unchanged

---

## 7. Migration Strategy

1. **Incremental Approach**: Create new structure alongside old, then migrate
2. **Test After Each Phase**: Run tests after each major phase
3. **Keep Old Code**: Don't delete old code until new code is verified
4. **Update Tests**: Migrate tests to new structure

---

## Next Steps

Proceeding with implementation starting with Phase 1: Core Domain creation.

