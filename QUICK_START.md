# Quick Start Guide - Running Test Automation Framework

This guide provides step-by-step instructions to run the automated tests.

## Prerequisites Check

Before starting, verify you have:

```bash
# Check Java version (should be 17 or higher)
java -version

# Check Maven version (should be 3.6+)
mvn -version

# Check Docker (should be installed and running)
docker --version
docker-compose --version
```

## Step-by-Step Execution

### Step 1: Navigate to Project Directory

```bash
cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
```

### Step 2: Start Docker Services (Selenium Grid & MySQL)

**Open a new terminal/PowerShell window and run:**

```bash
docker-compose up -d
```

This will start:
- Selenium Hub (http://localhost:4444)
- Chrome Node
- Firefox Node  
- MySQL Database (localhost:3306)
- Elasticsearch (localhost:9200) - optional
- Kibana (http://localhost:5601) - optional

**Wait 30-60 seconds for all services to start.**

**Verify services are running:**
```bash
docker-compose ps
```

You should see all containers in "Up" status.

**Check Selenium Grid UI:**
- Open browser: http://localhost:4444/ui
- You should see available nodes (Chrome and Firefox)

### Step 3: Verify Database is Ready

The database initializes automatically. To verify:

```bash
docker exec -it test-automation-mysql mysql -uroot -proot -e "USE testautomation; SELECT COUNT(*) FROM products;"
```

You should see 4 products.

### Step 4: Build the Project (First Time Only)

```bash
mvn clean install
```

This downloads dependencies and compiles the code. **Skip this step for subsequent runs.**

### Step 5: Run All Test Scenarios

**Option A: Run All Tests (Recommended)**
```bash
mvn clean test
```

**Option B: Run with TestNG**
```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

**Option C: Run Specific Scenario**

Run only Scenario 1 (Login Test):
```bash
mvn test -Dcucumber.filter.tags="@Scenario1"
```

Run only Scenario 2 (Login + API):
```bash
mvn test -Dcucumber.filter.tags="@Scenario2"
```

Run only Scenario 3 (Login + Database):
```bash
mvn test -Dcucumber.filter.tags="@Scenario3"
```

### Step 6: View Test Results

After execution completes, check the results:

#### 1. Cucumber HTML Report
```
Open: target/cucumber-reports/index.html
```
Right-click the file → Open with → Your web browser

#### 2. Screenshots
```
Location: target/screenshots/
```
View captured screenshots from test execution.

#### 3. Logs
```
Location: target/logs/automation.log
```
Check detailed execution logs.

#### 4. API Comparison Results
```
Location: target/api-comparison/
```
View API comparison files (for Scenario 2).

### Step 7: Stop Docker Services (When Done)

```bash
docker-compose down
```

To remove all data volumes:
```bash
docker-compose down -v
```

## Complete Execution Flow

Here's the complete flow in one sequence:

```bash
# 1. Start Docker services
docker-compose up -d

# 2. Wait for services (30-60 seconds)
# Check: http://localhost:4444/ui

# 3. Run tests
mvn clean test

# 4. View results
# - Open: target/cucumber-reports/index.html
# - Check: target/screenshots/
# - Check: target/logs/automation.log

# 5. Stop services
docker-compose down
```

## Troubleshooting Common Issues

### Issue: "Cannot connect to Selenium Grid"

**Solution:**
```bash
# Check if Docker services are running
docker-compose ps

# Check Selenium Hub logs
docker-compose logs selenium-hub

# Restart services
docker-compose restart
```

### Issue: "Database connection failed"

**Solution:**
```bash
# Check MySQL container
docker-compose logs mysql

# Verify MySQL is ready
docker exec -it test-automation-mysql mysql -uroot -proot -e "SELECT 1;"
```

### Issue: "Tests fail with timeout"

**Solution:**
1. Verify website is accessible: https://practicetestautomation.com/practice-test-login/
2. Check network connectivity
3. Increase timeout in `src/main/resources/application.properties`:
   ```properties
   selenium.timeout=60
   ```

### Issue: "Maven build fails"

**Solution:**
```bash
# Clean and rebuild
mvn clean
mvn install -U
```

### Issue: "Port 4444 already in use"

**Solution:**
```bash
# Stop existing containers
docker-compose down

# Check what's using the port (Windows)
netstat -ano | findstr :4444

# Kill the process or change port in docker-compose.yml
```

## Running Tests in Headless Mode

To run tests without opening browser windows:

1. Edit `src/main/resources/application.properties`
2. Change:
   ```properties
   selenium.headless=true
   ```
3. Run tests:
   ```bash
   mvn clean test
   ```

## Running Tests in Parallel

Tests are configured to run in parallel (4 threads). To change:

1. Edit `testng.xml`:
   ```xml
   <suite ... thread-count="2">
   ```
2. Or edit `pom.xml` in the surefire plugin configuration

## Expected Test Execution Time

- **Scenario 1**: ~30-60 seconds
- **Scenario 2**: ~60-90 seconds (includes API call)
- **Scenario 3**: ~60-90 seconds (includes database query)
- **All Scenarios**: ~3-5 minutes (with parallel execution)

## Success Indicators

✅ **Tests Passed:**
- Cucumber report shows green (passed) status
- Screenshots captured successfully
- Logs show no errors
- All scenarios completed

✅ **Docker Services:**
- All containers running (`docker-compose ps`)
- Selenium Grid UI shows nodes available
- MySQL container healthy

✅ **Results Generated:**
- HTML report created
- Screenshots in target/screenshots/
- Logs in target/logs/
- API comparison files (Scenario 2)

## Next Steps After Successful Execution

1. **Review Reports**: Analyze test results in Cucumber HTML report
2. **Check Screenshots**: Verify UI interactions captured correctly
3. **Review Logs**: Check for any warnings or issues
4. **Extend Tests**: Add new scenarios in feature files
5. **CI/CD**: Push to GitLab to trigger automated pipeline

## Quick Reference Commands

```bash
# Start services
docker-compose up -d

# Run all tests
mvn clean test

# Run specific scenario
mvn test -Dcucumber.filter.tags="@Scenario1"

# View Selenium Grid
# Browser: http://localhost:4444/ui

# Check logs
docker-compose logs -f

# Stop services
docker-compose down

# Clean everything
docker-compose down -v
mvn clean
```

## Need Help?

1. Check logs: `target/logs/automation.log`
2. Check Docker logs: `docker-compose logs`
3. Verify all prerequisites are installed
4. Review SETUP_GUIDE.md for detailed setup instructions
5. Check README.md for framework overview
