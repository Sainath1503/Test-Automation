# Test Automation Framework - Setup Guide

## Quick Start Guide

This guide will help you set up and run the Test Automation Framework on your local machine.

## Prerequisites Installation

### 1. Java 17
- Download and install Java 17 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://adoptium.net/)
- Verify installation:
  ```bash
  java -version
  ```

### 2. Maven 3.6+
- Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
- Set MAVEN_HOME environment variable
- Add Maven bin directory to PATH
- Verify installation:
  ```bash
  mvn -version
  ```

### 3. Docker Desktop
- Download and install Docker Desktop from [Docker](https://www.docker.com/products/docker-desktop)
- Verify installation:
  ```bash
  docker --version
  docker-compose --version
  ```

### 4. MySQL (Optional - Docker is recommended)
- If not using Docker, install MySQL 8.0+ from [MySQL](https://dev.mysql.com/downloads/mysql/)
- Default credentials: root/root

## Step-by-Step Setup

### Step 1: Clone/Download Project
```bash
cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
```

### Step 2: Start Docker Services

Start all required services using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- **Selenium Hub**: http://localhost:4444
- **Selenium Nodes**: Chrome and Firefox
- **MySQL Database**: localhost:3306
- **Elasticsearch**: localhost:9200 (optional)
- **Kibana**: http://localhost:5601 (optional)

Wait for all services to be ready (approximately 30-60 seconds).

**Verify Services:**
```bash
docker-compose ps
```

**Check Selenium Grid Status:**
- Open browser: http://localhost:4444/ui
- You should see available nodes

**Check MySQL:**
```bash
docker-compose logs mysql
```

### Step 3: Verify Database Initialization

The database should be initialized automatically. To verify:

```bash
docker exec -it test-automation-mysql mysql -uroot -proot -e "USE testautomation; SELECT * FROM products;"
```

You should see 4 products inserted.

### Step 4: Configure Application Properties

Edit `src/main/resources/application.properties` if needed:

```properties
# Verify these settings match your environment
spring.datasource.url=jdbc:mysql://localhost:3306/testautomation
spring.datasource.username=root
spring.datasource.password=root

selenium.grid.url=http://localhost:4444/wd/hub
selenium.browser=chrome
selenium.headless=false
```

### Step 5: Build the Project

```bash
mvn clean install
```

This will:
- Download all Maven dependencies
- Compile the source code
- Run unit tests (if any)
- Package the application

### Step 6: Run Tests

#### Run All Scenarios
```bash
mvn clean test
```

#### Run Specific Scenario
```bash
# Scenario 1: Login Test
mvn test -Dcucumber.filter.tags="@Scenario1"

# Scenario 2: Login with API Validation
mvn test -Dcucumber.filter.tags="@Scenario2"

# Scenario 3: Login with Database Validation
mvn test -Dcucumber.filter.tags="@Scenario3"
```

#### Run with TestNG
```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

## Viewing Results

### 1. Cucumber Reports
After test execution, open:
```
target/cucumber-reports/index.html
```
Open this file in a web browser to view detailed test results.

### 2. Screenshots
Screenshots are saved in:
```
target/screenshots/
```

### 3. Logs
Application logs are in:
```
target/logs/automation.log
```

### 4. API Comparison Results
API comparison files are in:
```
target/api-comparison/
```

### 5. Elasticsearch/Kibana (Optional)

If Elasticsearch is enabled:

1. Enable in `application.properties`:
   ```properties
   elasticsearch.enabled=true
   ```

2. Access Kibana dashboard:
   - URL: http://localhost:5601
   - Create index pattern: `test-automation-results`
   - View test results visualization

## Troubleshooting

### Issue: Docker services not starting

**Solution:**
```bash
# Check Docker Desktop is running
docker ps

# Stop all containers
docker-compose down

# Remove volumes and restart
docker-compose down -v
docker-compose up -d

# Check logs
docker-compose logs
```

### Issue: Selenium Grid connection failed

**Solution:**
1. Verify Selenium Hub is running:
   ```bash
   docker-compose ps selenium-hub
   ```
2. Check Grid UI: http://localhost:4444/ui
3. Verify nodes are registered
4. Check application.properties: `selenium.grid.url=http://localhost:4444/wd/hub`

### Issue: Database connection failed

**Solution:**
1. Verify MySQL is running:
   ```bash
   docker-compose ps mysql
   ```
2. Check MySQL logs:
   ```bash
   docker-compose logs mysql
   ```
3. Test connection:
   ```bash
   docker exec -it test-automation-mysql mysql -uroot -proot
   ```
4. Verify credentials in `application.properties`

### Issue: Tests failing with timeout

**Solution:**
1. Increase timeout in `application.properties`:
   ```properties
   selenium.timeout=60
   ```
2. Check if website is accessible: https://practicetestautomation.com/practice-test-login/
3. Verify network connectivity

### Issue: Maven build fails

**Solution:**
1. Clean Maven cache:
   ```bash
   mvn clean
   rm -rf ~/.m2/repository  # Linux/Mac
   # or
   rmdir /s C:\Users\%USERNAME%\.m2\repository  # Windows
   ```
2. Rebuild:
   ```bash
   mvn clean install -U
   ```

### Issue: Spring context not loading

**Solution:**
1. Verify Spring Boot application class exists
2. Check component scan path in `TestAutomationFrameworkApplication.java`
3. Verify all dependencies in `pom.xml` are correct
4. Check logs for specific error messages

## GitLab CI/CD Setup

### Prerequisites
- GitLab account with CI/CD enabled
- Docker executor configured
- GitLab Runner with Docker support

### Configuration
The `.gitlab-ci.yml` file is already configured. To use it:

1. Push code to GitLab repository
2. Configure GitLab CI/CD variables (if needed):
   - No variables required by default
3. Pipeline will trigger automatically on merge

### Manual Pipeline Trigger
- Go to CI/CD → Pipelines
- Click "Run Pipeline"

## Next Steps

1. **Customize Test Data**: Edit feature files in `src/test/resources/features/`
2. **Add New Pages**: Create page objects in `src/main/java/.../pages/`
3. **Extend API Tests**: Add methods in `RestApiClient.java`
4. **Add Database Tests**: Create entities and repositories
5. **Configure Reporting**: Set up Elasticsearch/Kibana for advanced reporting

## Support

For issues or questions:
1. Check logs: `target/logs/automation.log`
2. Review error messages in test reports
3. Verify all prerequisites are installed
4. Check Docker services are running

## Architecture Documentation

For detailed architecture information, see `ARCHITECTURE.md`
