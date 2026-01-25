# Test Automation Framework - Complete Setup Guide

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Quick Start](#quick-start)
4. [Detailed Setup Instructions](#detailed-setup-instructions)
5. [Shell Scripts Reference](#shell-scripts-reference)
6. [Running Tests](#running-tests)
7. [Project Structure](#project-structure)
8. [Troubleshooting](#troubleshooting)
9. [Scalability Assessment](#scalability-assessment)

---

## Overview

This is a comprehensive Test Automation Framework built with:
- **Java 17** and **Spring Boot 3.1.5**
- **Maven** for dependency management
- **TestNG** and **Cucumber** for BDD testing
- **Selenium Grid** for web automation
- **Rest-Assured** for API testing
- **Docker** for containerization
- **MySQL** for database testing
- **Elasticsearch/Kibana** for reporting (optional)

The framework supports:
- Web UI automation
- REST API testing
- Database validation
- Parallel test execution
- BDD-style test scenarios

---

## Prerequisites

### Required Software

1. **Java 17 or higher**
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **Maven 3.6+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **Docker and Docker Compose**
   - Docker Desktop: https://www.docker.com/products/docker-desktop
   - Verify: `docker --version` and `docker compose version`

4. **Git** (optional but recommended)
   - Download from: https://git-scm.com/downloads

### System Requirements

- **Minimum RAM**: 4GB (8GB recommended)
- **Disk Space**: 5GB free space
- **OS**: Linux, macOS, or Windows (with WSL2 for Docker)

---

## Quick Start

### Option 1: Automated Setup (Recommended)

1. **Clone the repository** (if not already done):
   ```bash
   git clone <repository-url>
   cd Test-Automation
   ```

2. **Check prerequisites**:
   ```bash
   ./check-prerequisites.sh
   ```

3. **Run automated setup** (Linux/macOS):
   ```bash
   ./setup.sh
   ```

4. **Pull Docker containers**:
   ```bash
   ./pull-containers.sh
   ```

5. **Run the project**:
   ```bash
   ./run-project.sh
   ```

### Option 2: Manual Setup

Follow the [Detailed Setup Instructions](#detailed-setup-instructions) below.

---

## Detailed Setup Instructions

### Step 1: Install Prerequisites

#### Linux (Ubuntu/Debian)

```bash
# Install Java 17
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk

# Install Maven
sudo apt-get install -y maven

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
# Log out and log back in for group changes to take effect
```

#### macOS

```bash
# Install Homebrew (if not installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java 17
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@17"' >> ~/.zshrc

# Install Maven
brew install maven

# Install Docker Desktop
brew install --cask docker
```

#### Windows

1. Install Java 17 from https://adoptium.net/
2. Install Maven from https://maven.apache.org/download.cgi
3. Install Docker Desktop from https://www.docker.com/products/docker-desktop
4. Ensure WSL2 is enabled for Docker Desktop

### Step 2: Verify Installation

Run the prerequisite check script:

```bash
./check-prerequisites.sh
```

All checks should pass before proceeding.

### Step 3: Pull Docker Images

Pull all required Docker images:

```bash
./pull-containers.sh
```

This will download:
- Selenium Hub and Nodes (Chrome, Firefox)
- MySQL 8.0
- Elasticsearch 8.10.1 (optional)
- Kibana 8.10.1 (optional)

### Step 4: Configure the Project

Edit `src/main/resources/application.properties` if needed:

```properties
# Database Configuration
# Note: Using port 3307 to avoid conflict with local MySQL on 3306
spring.datasource.url=jdbc:mysql://localhost:3307/testautomation?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root

# Selenium Grid Configuration
selenium.grid.url=http://localhost:4444/wd/hub
selenium.browser=chrome

# Application URLs
web.url=https://practicetestautomation.com/practice-test-login/
api.url=https://automationexercise.com/api/productsList

# Elasticsearch (set to false to disable)
elasticsearch.enabled=true
```

### Step 5: Build the Project

```bash
mvn clean install
```

### Step 6: Run Tests

```bash
./run-project.sh
```

Or manually:

```bash
# Start containers
docker-compose up -d

# Wait for services to be ready
sleep 30

# Run tests
mvn clean test
```

---

## Shell Scripts Reference

### check-prerequisites.sh

**Purpose**: Verifies all required tools are installed and properly configured.

**Usage**:
```bash
./check-prerequisites.sh
```

**What it checks**:
- Java 17+ installation
- Maven 3.6+ installation
- Docker and Docker Compose availability
- Docker daemon status
- Git (optional)

**Exit Codes**:
- `0`: All prerequisites met
- `1`: Missing prerequisites

---

### setup.sh

**Purpose**: Automatically installs missing prerequisites (Linux/macOS only).

**Usage**:
```bash
./setup.sh
```

**What it does**:
- Detects your operating system
- Prompts to install missing Java, Maven, or Docker
- Creates necessary project directories
- Runs final prerequisite check

**Note**: On Windows, manual installation is required.

---

### pull-containers.sh

**Purpose**: Pulls all Docker images required by the project.

**Usage**:
```bash
./pull-containers.sh
```

**What it does**:
- Checks Docker daemon status
- Pulls images defined in `docker-compose.yml`:
  - Selenium Hub (selenium/hub:4.15.0)
  - Selenium Chrome Node (selenium/node-chrome:4.15.0)
  - Selenium Firefox Node (selenium/node-firefox:4.15.0)
  - MySQL 8.0 (mysql:8.0)
  - Elasticsearch 8.10.1
  - Kibana 8.10.1

**Prerequisites**: Docker must be running.

---

### run-project.sh

**Purpose**: Starts Docker containers, builds the project, and runs tests.

**Usage**:
```bash
# Run all tests
./run-project.sh

# Run specific test tag
./run-project.sh "@Scenario1"
```

**What it does**:
1. Starts all Docker containers (Selenium Grid, MySQL, Elasticsearch, Kibana)
2. Waits for services to be ready
3. Verifies containers are running
4. Builds the project (Maven compile)
5. Runs tests (Maven test)
6. Displays test results and report locations

**Service URLs** (after running):
- Selenium Grid UI: http://localhost:4444/ui
- Selenium Status: http://localhost:4444/status
- MySQL: localhost:3307 (using 3307 to avoid conflict with local MySQL on 3306)
- Elasticsearch: http://localhost:9200
- Kibana: http://localhost:5933

---

### cleanup.sh

**Purpose**: Stops Docker containers and optionally cleans up build artifacts.

**Usage**:
```bash
# Stop containers only
./cleanup.sh

# Stop containers and remove volumes
./cleanup.sh --volumes

# Stop containers and clean build artifacts
./cleanup.sh --build

# Full cleanup (containers, volumes, build, images)
./cleanup.sh --all
```

**Options**:
- `--volumes` or `-v`: Remove Docker volumes
- `--build` or `-b`: Clean Maven build artifacts
- `--all` or `-a`: Full cleanup including Docker images

---

### cleanup-containers.sh

**Purpose**: Forcefully removes all project containers, including orphaned ones. Useful when containers are stuck or have naming conflicts.

**Usage**:
```bash
./cleanup-containers.sh
```

**What it does**:
- Stops and removes containers using docker-compose
- Removes containers by name (handles orphaned containers)
- Prompts to remove any remaining project containers

**Use this when**:
- You get "container name already in use" errors
- Containers are stuck or not responding
- You need a clean slate before starting containers

---

## Running Tests

### Run All Tests

```bash
./run-project.sh
```

Or manually:
```bash
mvn clean test
```

### Run Specific Test Scenario

```bash
# Scenario 1: Login Test
mvn test -Dcucumber.filter.tags="@Scenario1"

# Scenario 2: Login with API Validation
mvn test -Dcucumber.filter.tags="@Scenario2"

# Scenario 3: Login with Database Validation
mvn test -Dcucumber.filter.tags="@Scenario3"
```

### Run with TestNG XML

```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

### Parallel Execution

Tests are configured to run in parallel with 4 threads (configurable in `testng.xml`):

```xml
<suite name="Test Automation Framework Suite"
       parallel="methods"
       thread-count="4">
```

---

## Project Structure

```
Test-Automation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/automention/framework/
│   │   │       ├── api/              # API testing utilities
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── driver/           # WebDriver management
│   │   │       ├── entity/           # Database entities
│   │   │       ├── pages/            # Page Object Models
│   │   │       ├── repository/       # Data repositories
│   │   │       ├── service/          # Business services
│   │   │       └── utils/            # Utility classes
│   │   └── resources/
│   │       ├── application.properties # Application configuration
│   │       ├── log4j2.xml            # Logging configuration
│   │       └── database/
│   │           └── init.sql          # Database initialization
│   └── test/
│       ├── java/
│       │   └── com/automention/framework/
│       │       ├── config/           # Test configuration
│       │       ├── listeners/        # Test listeners
│       │       ├── runners/          # Test runners
│       │       ├── stepdefinitions/  # Cucumber step definitions
│       │       └── utils/            # Test utilities
│       └── resources/
│           ├── features/             # BDD feature files
│           └── api/                  # API test data
├── docker-compose.yml                # Docker services configuration
├── Dockerfile                        # Application Dockerfile
├── pom.xml                           # Maven configuration
├── testng.xml                        # TestNG suite configuration
├── check-prerequisites.sh            # Prerequisite check script
├── setup.sh                          # Automated setup script
├── pull-containers.sh                # Pull Docker images script
├── run-project.sh                    # Run project script
├── cleanup.sh                        # Cleanup script
├── README.md                         # Project overview
├── SETUP.md                          # Original setup guide
└── SETUP_GUIDE.md                    # This comprehensive guide
```

---

## Test Reports

After running tests, reports are generated in:

1. **Cucumber HTML Report**: `target/cucumber-report.html`
2. **TestNG Reports**: `target/surefire-reports/index.html`
3. **Screenshots**: `target/screenshots/`
4. **Logs**: `target/logs/automation.log`
5. **API Comparison Results**: `target/api-comparison/`

### Viewing Reports

Open the HTML reports in your browser:

```bash
# Linux/macOS
open target/cucumber-report.html
# or
xdg-open target/cucumber-report.html

# Windows
start target/cucumber-report.html
```

---

## Troubleshooting

### Issue: Docker daemon is not running

**Solution**:
```bash
# Linux
sudo systemctl start docker

# macOS/Windows
# Start Docker Desktop application
```

### Issue: Container name conflict

**Error**: `Cannot create container for service selenium-hub: Conflict. The container name "/selenium-hub" is already in use`

**Solution**:
```bash
# Option 1: Use the cleanup script
./cleanup-containers.sh

# Option 2: Manual cleanup
docker-compose down
docker rm -f selenium-hub selenium-node-chrome selenium-node-firefox test-automation-mysql elasticsearch kibana

# Then retry
./run-project.sh
```

### Issue: Port already in use

**Error**: `Bind for 0.0.0.0:4444 failed: port is already allocated`

**Solution**:
```bash
# Find process using the port
lsof -i :4444  # macOS/Linux
netstat -ano | findstr :4444  # Windows

# Stop the process or change port in docker-compose.yml
```

### Issue: Maven build fails

**Possible causes**:
- Java version mismatch (need Java 17+)
- Network issues downloading dependencies
- Corrupted Maven repository

**Solution**:
```bash
# Clean Maven cache
rm -rf ~/.m2/repository

# Rebuild
mvn clean install
```

### Issue: Selenium Grid not ready

**Error**: `Unable to connect to Selenium Grid`

**Solution**:
```bash
# Check container status
docker-compose ps

# Check Selenium Hub logs
docker-compose logs selenium-hub

# Wait longer for services to start
# Edit run-project.sh and increase wait time
```

### Issue: Database connection fails

**Error**: `Communications link failure`

**Solution**:
```bash
# Check MySQL container
docker-compose ps mysql
docker-compose logs mysql

# Verify database credentials in application.properties
# Ensure MySQL is fully initialized (wait 30-60 seconds after start)
```

### Issue: Tests fail with timeout

**Solution**:
- Increase timeout in `application.properties`:
  ```properties
  selenium.timeout=60
  ```
- Check Selenium Grid has available nodes: http://localhost:4444/ui
- Verify network connectivity

### Issue: Permission denied on scripts

**Solution**:
```bash
chmod +x *.sh
```

---

## Scalability Assessment

### Current Architecture

The framework is designed with scalability in mind:

#### Scalable Aspects

1. **Containerized Services**
   - All services run in Docker containers
   - Easy to scale horizontally
   - Can deploy to Kubernetes/cloud platforms

2. **Selenium Grid**
   - Supports multiple browser nodes
   - Can add more nodes for parallel execution
   - Current config: 4 Chrome + 4 Firefox instances

3. **Parallel Test Execution**
   - TestNG parallel execution (4 threads)
   - Can increase thread count in `testng.xml`
   - Cucumber supports parallel scenarios

4. **Modular Architecture**
   - Page Object Model pattern
   - Separation of concerns (API, DB, UI)
   - Easy to extend with new test scenarios

5. **CI/CD Ready**
   - GitLab CI/CD pipeline configured
   - Can integrate with Jenkins, GitHub Actions, etc.
   - Docker-based execution

#### Scalability Limitations

1. **Single MySQL Instance**
   - Currently uses single MySQL container
   - For high load, consider MySQL cluster or cloud database

2. **Local Selenium Grid**
   - Single hub with limited nodes
   - For large-scale testing, use cloud Selenium Grid (Sauce Labs, BrowserStack) or Kubernetes

3. **Elasticsearch Single Node**
   - Currently single-node Elasticsearch
   - For production, use Elasticsearch cluster

4. **Resource Constraints**
   - Running all services locally requires significant RAM (8GB+ recommended)
   - Consider cloud deployment for resource-intensive scenarios

### Recommendations for Scaling

1. **Cloud Deployment**
   - Deploy to AWS, Azure, or GCP
   - Use managed services (RDS for MySQL, Elasticsearch Service)
   - Use cloud Selenium Grid or Kubernetes

2. **Kubernetes Deployment**
   - Convert docker-compose.yml to Kubernetes manifests
   - Use Horizontal Pod Autoscaler for Selenium nodes
   - Deploy MySQL and Elasticsearch as StatefulSets

3. **Distributed Testing**
   - Split test suites across multiple runners
   - Use test sharding strategies
   - Implement test result aggregation

4. **Performance Optimization**
   - Use test data factories instead of database seeding
   - Implement test result caching
   - Use headless browsers for faster execution

5. **Monitoring and Observability**
   - Add Prometheus/Grafana for metrics
   - Implement distributed tracing
   - Set up alerting for test failures

### Scalability Rating

**Overall Scalability: 7/10**

- **Small to Medium Scale**: Excellent (1-100 parallel tests)
- **Large Scale**: Good with modifications (100-1000 parallel tests)
- **Enterprise Scale**: Requires cloud/Kubernetes deployment (1000+ parallel tests)

### Conclusion

The framework is **well-architected for small to medium-scale testing** and can be **scaled to enterprise level** with cloud deployment and infrastructure modifications. The modular design and containerization make it relatively easy to scale horizontally.

---

## Additional Resources

- [Maven Documentation](https://maven.apache.org/guides/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [Selenium Grid Documentation](https://www.selenium.dev/documentation/grid/)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

---

## Support

For issues or questions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review project documentation (README.md, SETUP.md)
3. Check container logs: `docker-compose logs <service-name>`
4. Contact the development team

---

**Last Updated**: January 2025
