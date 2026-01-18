# Test Automation Framework Architecture

## Overview

This framework follows **Spring Boot Architecture** principles for easy code extension and maintainability. The architecture is designed to be modular, scalable, and follows industry best practices.

## Architecture Layers

### 1. Configuration Layer (`com.automention.framework.config`)

**Purpose**: Centralized configuration management

**Components**:
- `ApplicationConfig`: Loads and provides access to application properties
- `SpringConfig`: Spring bean configuration
- `CucumberSpringConfiguration`: Enables Spring context for Cucumber tests

**Responsibilities**:
- Property file management
- Environment-specific configurations
- Bean definitions and dependency injection setup

### 2. Driver Layer (`com.automention.framework.driver`)

**Purpose**: WebDriver management and lifecycle

**Components**:
- `WebDriverManager`: Manages WebDriver instances using ThreadLocal for parallel execution
- Supports Selenium Grid integration
- Handles browser capabilities configuration

**Key Features**:
- Thread-safe WebDriver management
- Selenium Grid connectivity
- Multiple browser support (Chrome, Firefox, Edge)
- Automatic cleanup on test completion

### 3. Page Object Layer (`com.automention.framework.pages`)

**Purpose**: Page Object Model (POM) implementation

**Components**:
- `LoginPage`: Login page interactions and validations

**Design Pattern**: Page Object Model (POM)
- Encapsulates page elements and actions
- Reusable page methods
- Separation of test logic from page interactions

### 4. API Layer (`com.automention.framework.api`)

**Purpose**: REST API testing utilities

**Components**:
- `RestApiClient`: REST API request/response handling using Rest-Assured
- `JsonComparisonUtil`: JSON comparison using SkyScreamer library

**Key Features**:
- REST API endpoint testing
- JSON response validation
- API response comparison (missing/additional fields)
- Comparison results saved to files

### 5. Data Access Layer (`com.automention.framework.entity`, `repository`, `service`)

**Purpose**: Database operations using Hibernate/JPA

**Components**:
- `Product`: Entity class mapping to database table
- `ProductRepository`: JPA repository interface
- `DatabaseService`: Business logic for database operations

**Technology Stack**:
- Spring Data JPA
- Hibernate ORM
- MySQL Database

### 6. Utility Layer (`com.automention.framework.utils`)

**Purpose**: Reusable utility classes

**Components**:
- `ExcelConfigReader`: Excel file reading using Apache POI
- `ScreenshotUtil`: Screenshot capture functionality
- `LoggerUtil`: Logging utility wrapper
- `ElasticSearchUtil`: Elasticsearch integration for reporting
- `FileUtil`: File operations

### 7. Test Layer (`src/test/java/com/automention/framework`)

**Purpose**: Test execution and BDD implementation

**Components**:
- **Features** (`resources/features`): Gherkin feature files
- **Step Definitions** (`stepdefinitions`): Cucumber step implementations
- **Runners** (`runners`): TestNG test runners
- **Hooks** (`hooks`): Cucumber before/after hooks
- **Listeners** (`listeners`): TestNG listeners for reporting
- **Config** (`config`): Test-specific configurations

## Design Patterns Used

### 1. Page Object Model (POM)
- Encapsulates web page elements and actions
- Promotes code reusability
- Reduces maintenance effort

### 2. Dependency Injection (Spring)
- Loose coupling between components
- Easy testing and mocking
- Configuration management

### 3. Factory Pattern
- WebDriver creation and management
- Browser capabilities configuration

### 4. Singleton Pattern
- Application configuration
- Logging utilities

### 5. Repository Pattern
- Data access abstraction
- Database operations encapsulation

## Framework Flow

### Test Execution Flow

```
Feature File (Gherkin)
    ↓
Step Definitions (Java)
    ↓
Page Objects / API Clients / Services
    ↓
WebDriver / REST Client / Database
    ↓
Application Under Test
```

### Parallel Execution

- **TestNG** handles parallel test execution
- **ThreadLocal** WebDriver instances for thread safety
- Configurable thread count (default: 4 threads)
- Isolated test execution per thread

### Reporting Flow

```
Test Execution
    ↓
Test Listener
    ↓
Screenshot Capture
    ↓
Elasticsearch Indexing
    ↓
Kibana Visualization
```

## Extension Points

### Adding New Test Scenarios

1. Create feature file in `src/test/resources/features/`
2. Add step definitions in `src/test/java/.../stepdefinitions/`
3. Implement page objects if needed
4. Add tags for selective execution

### Adding New Pages

1. Create page class in `com.automention.framework.pages`
2. Use `@Component` annotation
3. Implement PageFactory pattern
4. Add reusable methods for page interactions

### Adding New API Endpoints

1. Extend `RestApiClient` with new methods
2. Use `JsonComparisonUtil` for validation
3. Create step definitions for API testing

### Adding New Database Operations

1. Create entity class in `com.automention.framework.entity`
2. Create repository interface extending `JpaRepository`
3. Create service class in `com.automention.framework.service`
4. Use service in step definitions

## Configuration Management

### Properties Files
- `application.properties`: Main configuration
- Environment-specific properties can be added
- Spring profiles for different environments

### Excel Configuration
- Test data stored in Excel files
- `ExcelConfigReader` utility for reading data
- Configurable Excel file path

## Dependency Management

- **Maven**: Centralized dependency management
- **Spring Boot Parent**: Version management
- **Managed Dependencies**: Consistent versions

## Testing Strategy

### Unit Testing
- Service layer unit tests
- Utility class tests

### Integration Testing
- Database integration tests
- API integration tests

### End-to-End Testing
- BDD scenarios with Cucumber
- Web application testing
- Full workflow validation

## CI/CD Integration

### GitLab CI/CD Pipeline
1. **Build Stage**: Compile and package
2. **Test Stage**: Execute all test scenarios
3. **Artifact Collection**: Screenshots, reports, logs
4. **Docker Integration**: Selenium Grid, MySQL, Elasticsearch

## Best Practices Implemented

1. **Separation of Concerns**: Clear layer separation
2. **DRY Principle**: Reusable utilities and components
3. **SOLID Principles**: Single responsibility, dependency inversion
4. **Logging**: Comprehensive logging with Log4j2
5. **Error Handling**: Proper exception handling
6. **Thread Safety**: ThreadLocal for parallel execution
7. **Configuration Externalization**: Properties-based configuration
8. **Documentation**: Comprehensive code documentation

## Future Enhancements

1. Add more browser support
2. Extend API testing capabilities
3. Add mobile testing support
4. Enhanced reporting dashboards
5. Performance testing integration
6. Test data management improvements
7. Continuous test execution monitoring
