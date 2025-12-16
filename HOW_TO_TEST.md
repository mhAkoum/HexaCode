# How to Test the Hexagonal Architecture Code

## Quick Start

### 1. Run All Tests
```bash
./gradlew test
```

### 2. Run Specific Test Class
```bash
# Unit test for use case
./gradlew test --tests "AccountUseCaseTest"

# Integration test
./gradlew test --tests "ClientControllerIntegrationTest"
```

### 3. Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
# View report at: build/reports/jacoco/test/html/index.html
```

## Test Types

### ✅ Unit Tests (Fast, No Spring Context)

**Location**: `src/test/java/com/simplecash/projet_akoum_mohamad/application/usecase/`

**Example**: `AccountUseCaseTest`, `ClientUseCaseTest`

**Characteristics**:
- Test use cases in isolation
- Mock all port.out dependencies
- No Spring context needed
- Very fast execution
- Test business logic only

**Run**:
```bash
./gradlew test --tests "*UseCaseTest"
```

### 🔌 Integration Tests (Full Spring Context)

**Location**: `src/test/java/com/simplecash/projet_akoum_mohamad/adapter/in/web/controller/`

**Example**: `ClientControllerIntegrationTest`, `AccountControllerIntegrationTest`

**Characteristics**:
- Test full application stack
- Use real database (H2 in-memory)
- Test REST endpoints end-to-end
- Slower execution

**Run**:
```bash
./gradlew test --tests "*IntegrationTest"
```

## Testing Workflow

### Step 1: Test Use Cases (Unit Tests)
```bash
# Test account operations
./gradlew test --tests "AccountUseCaseTest"

# Test client operations
./gradlew test --tests "ClientUseCaseTest"
```

### Step 2: Test Adapters (Integration Tests)
```bash
# Test persistence adapters
./gradlew test --tests "*PersistenceAdapterTest"

# Test web controllers
./gradlew test --tests "*ControllerIntegrationTest"
```

### Step 3: Test Full Application
```bash
# Run all tests
./gradlew test
```

## Example Test Scenarios

### Test Account Credit Operation
```bash
./gradlew test --tests "AccountUseCaseTest.testCreditAccount_Success"
```

### Test Client Creation with Business Rules
```bash
./gradlew test --tests "ClientUseCaseTest.testCreateClient_AdvisorFull"
```

### Test REST Endpoint
```bash
./gradlew test --tests "ClientControllerIntegrationTest.testCreateClient_Success"
```

## Debugging Tests

### Run Tests with Debug Output
```bash
./gradlew test --info
```

### Run Single Test Method
```bash
./gradlew test --tests "AccountUseCaseTest.testCreditAccount_Success" --debug
```

### View Test Reports
```bash
# HTML report
open build/reports/tests/test/index.html
```

## Common Issues

### Issue: Tests Fail with Bean Conflicts
**Solution**: Old code is excluded via `@ComponentScan` filters in `ProjetAkoumMohamadApplication.java`

### Issue: Tests Can't Find Beans
**Solution**: Make sure you're using the new adapter packages:
- Controllers: `adapter.in.web.controller`
- Repositories: `adapter.out.persistence.repository`

### Issue: Domain Model Not Found
**Solution**: Use domain models from `domain.model` package, not the old `domain` package

## Best Practices

1. **Write Unit Tests First**: Test use cases with mocked ports
2. **Keep Tests Fast**: Unit tests should run in milliseconds
3. **Test Business Rules**: Focus on business logic in use cases
4. **Use Integration Tests Sparingly**: Only for critical paths
5. **Mock External Dependencies**: Always mock port.out in unit tests

## Test Structure

```
src/test/java/com/simplecash/projet_akoum_mohamad/
├── application/
│   └── usecase/              # Unit tests for use cases
│       ├── AccountUseCaseTest.java
│       ├── ClientUseCaseTest.java
│       └── TransferUseCaseTest.java
├── adapter/
│   ├── in/web/controller/    # Integration tests for controllers
│   │   ├── ClientControllerIntegrationTest.java
│   │   └── AccountControllerIntegrationTest.java
│   └── out/persistence/      # Integration tests for adapters
│       └── adapter/
│           └── ClientPersistenceAdapterTest.java
```

## Continuous Integration

Add to your CI/CD pipeline:
```bash
./gradlew clean test
```

This ensures all tests pass before deployment.

