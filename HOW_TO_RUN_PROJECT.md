# How to Run the Project

## Quick Start

The fastest way to run the project:

```bash
./quick-start.sh
```

This single command will:
1. Check prerequisites
2. Pull Docker containers
3. Start all services
4. Build the project
5. Run all tests

---

## Running Tests

### Run All Tests

```bash
./run-project.sh
```

Or manually:
```bash
# Start services
docker-compose up -d

# Wait for services to be ready
sleep 30

# Run tests
mvn clean test
```

### Run Specific Test Scenario

```bash
# Scenario 1: Login Test
./run-project.sh "@Scenario1"

# Or manually
mvn test -Dcucumber.filter.tags="@Scenario1"

# Scenario 2: Login with API Validation
./run-project.sh "@Scenario2"

# Scenario 3: Login with Database Validation
./run-project.sh "@Scenario3"
```

### Run with TestNG XML

```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

---

## Viewing Results

### Cucumber HTML Report

```bash
# Linux
xdg-open target/cucumber-report.html

# macOS
open target/cucumber-report.html

# Windows
start target/cucumber-report.html
```

### TestNG Reports

```bash
# Linux
xdg-open target/surefire-reports/index.html

# macOS
open target/surefire-reports/index.html

# Windows
start target/surefire-reports/index.html
```

### Screenshots

Screenshots are saved in: `target/screenshots/`

### Logs

Application logs: `target/logs/automation.log`

### API Comparison Results

API comparison results: `target/api-comparison/`

---

## Understanding Output

### Test Execution Output

When tests run, you'll see:

1. **Maven build output**: Compilation and dependency resolution
2. **Test execution**: Cucumber scenarios running
3. **Test results**: Pass/fail status for each scenario
4. **Summary**: Total tests, passed, failed, skipped

### Report Locations

After test execution, reports are generated in:

- **Cucumber HTML**: `target/cucumber-report.html`
- **Cucumber JSON**: `target/cucumber.json`
- **Cucumber XML**: `target/cucumber.xml`
- **TestNG HTML**: `target/surefire-reports/index.html`
- **TestNG XML**: `target/surefire-reports/TEST-TestSuite.xml`

### Service Status

Check service status:

```bash
# Docker containers
docker-compose ps

# Selenium Grid status
curl http://localhost:4444/status

# Elasticsearch status (if enabled)
curl http://localhost:9200
```

---

## Service URLs

After starting the project, access services at:

- **Selenium Grid UI**: http://localhost:4444/ui
- **Selenium Status**: http://localhost:4444/status
- **MySQL**: localhost:3307
- **Elasticsearch**: http://localhost:9200 (if enabled)
- **Kibana**: http://localhost:5933 (if enabled)

---

## Common Commands

### Start Services Only

```bash
docker-compose up -d
```

### Stop Services

```bash
docker-compose down
```

Or use the cleanup script:
```bash
./cleanup.sh
```

### Restart Services

```bash
docker-compose restart
```

### View Service Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f selenium-hub
docker-compose logs -f mysql
```

### Rebuild and Run

```bash
# Clean build and run
mvn clean test

# Skip tests, just build
mvn clean install -DskipTests
```

---

## Parallel Execution

Tests are configured to run in parallel with 4 threads (configurable in `testng.xml`):

```xml
<suite name="Test Automation Framework Suite"
       parallel="methods"
       thread-count="4">
```

To change the thread count, edit `testng.xml` and update `thread-count`.

---

## Troubleshooting

### Tests Not Running

1. **Check services are running**:
   ```bash
   docker-compose ps
   ```

2. **Check Selenium Grid is ready**:
   ```bash
   curl http://localhost:4444/status
   ```

3. **Wait for services to be ready** (may take 30-60 seconds after start)

### Tests Failing

1. **Check logs**: `target/logs/automation.log`
2. **Check screenshots**: `target/screenshots/`
3. **Check service logs**: `docker-compose logs <service-name>`
4. **Verify configuration**: `src/main/resources/application.properties`

### Services Not Starting

1. **Check port conflicts**: See [PORT_CONFIGURATION.md](PORT_CONFIGURATION.md)
2. **Check Docker daemon**: `docker info`
3. **Clean up containers**: `./cleanup-containers.sh`

For more troubleshooting, see [TROUBLESHOOTING.md](TROUBLESHOOTING.md).

---

## Next Steps

- **View detailed setup**: [SETUP_GUIDE.md](SETUP_GUIDE.md)
- **Understand Docker**: See Docker section in SETUP_GUIDE.md
- **Configure ports**: [PORT_CONFIGURATION.md](PORT_CONFIGURATION.md)
- **Troubleshoot issues**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

**Last Updated**: January 2025
