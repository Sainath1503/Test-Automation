# How to Run Test Automation Framework - Step by Step

## ✅ Prerequisites Status

Based on your system:
- ✅ **Docker**: Installed (version 28.4.0)
- ✅ **Java**: Installed (version 24.0.2)
- ❌ **Maven**: Not in PATH (needs to be installed/configured)

## Quick Start (3 Steps)

### Step 1: Install/Configure Maven (If Not Already Installed)

**Option A: Install Maven (Recommended)**

1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to a folder (e.g., `C:\Program Files\Apache\maven`)
3. Add to System PATH:
   - Open System Properties → Environment Variables
   - Add `C:\Program Files\Apache\maven\bin` to PATH
   - Restart PowerShell/Command Prompt

**Option B: Use Maven Wrapper (If Available)**

Some projects include Maven Wrapper. Check if `mvnw.cmd` exists in the project.

**Verify Maven Installation:**
```powershell
mvn -version
```

### Step 2: Start Docker Services

Open PowerShell in the project directory and run:

```powershell
docker-compose up -d
```

**Wait 30-60 seconds** for services to start.

**Verify Services:**
```powershell
docker-compose ps
```

You should see containers for:
- selenium-hub
- selenium-node-chrome
- selenium-node-firefox
- test-automation-mysql
- elasticsearch (optional)
- kibana (optional)

**Check Selenium Grid:**
- Open browser: http://localhost:4444/ui
- You should see available nodes

### Step 3: Run Tests

**Navigate to project directory:**
```powershell
cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
```

**Run all test scenarios:**
```powershell
mvn clean test
```

**Or run specific scenario:**
```powershell
# Scenario 1 only
mvn test -Dcucumber.filter.tags="@Scenario1"

# Scenario 2 only  
mvn test -Dcucumber.filter.tags="@Scenario2"

# Scenario 3 only
mvn test -Dcucumber.filter.tags="@Scenario3"
```

## What Happens During Test Execution

1. **Maven downloads dependencies** (first time only)
2. **Compiles Java code**
3. **Starts Spring Boot context**
4. **Executes BDD scenarios:**
   - Scenario 1: Web login test
   - Scenario 2: Web login + API validation
   - Scenario 3: Web login + Database validation
5. **Generates reports and screenshots**

## Viewing Test Results

### 1. Cucumber HTML Report (Main Report)

After tests complete, open:
```
target\cucumber-reports\index.html
```

**How to open:**
- Navigate to the folder in File Explorer
- Right-click `index.html`
- Select "Open with" → Choose your browser

### 2. Screenshots

Location: `target\screenshots\`

Each test captures screenshots at key steps:
- Login success
- Logout
- Failures

### 3. Logs

Location: `target\logs\automation.log`

Contains detailed execution logs with timestamps.

### 4. API Comparison Results (Scenario 2)

Location: `target\api-comparison\`

Contains text files with API comparison results (missing/additional fields).

## Complete Execution Example

Here's the complete flow in PowerShell:

```powershell
# 1. Navigate to project
cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"

# 2. Start Docker services (in background)
docker-compose up -d

# 3. Wait a moment, then verify services
docker-compose ps

# 4. Build project (first time only)
mvn clean install

# 5. Run all tests
mvn clean test

# 6. View results
# Open: target\cucumber-reports\index.html in browser

# 7. Stop Docker services (when done)
docker-compose down
```

## Troubleshooting

### Issue: "mvn is not recognized"

**Solution:** Install Maven or add to PATH
- Download: https://maven.apache.org/download.cgi
- Add bin folder to System PATH
- Restart PowerShell

### Issue: Docker services won't start

**Solution:**
```powershell
# Check if Docker Desktop is running
docker ps

# Stop existing containers
docker-compose down

# Start fresh
docker-compose up -d

# Check logs
docker-compose logs
```

### Issue: Port 4444 already in use

**Solution:**
```powershell
# Find what's using port 4444
netstat -ano | findstr :4444

# Stop Docker services
docker-compose down

# Or change port in docker-compose.yml
```

### Issue: Tests fail to connect to Selenium Grid

**Solution:**
1. Verify Selenium Hub is running:
   ```powershell
   docker-compose ps selenium-hub
   ```
2. Check Grid UI: http://localhost:4444/ui
3. Verify nodes are registered
4. Check application.properties: `selenium.grid.url=http://localhost:4444/wd/hub`

### Issue: Database connection fails

**Solution:**
```powershell
# Check MySQL container
docker-compose logs mysql

# Verify MySQL is ready
docker exec -it test-automation-mysql mysql -uroot -proot -e "SELECT 1;"

# Restart MySQL
docker-compose restart mysql
```

### Issue: Tests timeout

**Solution:**
1. Increase timeout in `src\main\resources\application.properties`:
   ```properties
   selenium.timeout=60
   ```
2. Verify website is accessible: https://practicetestautomation.com/practice-test-login/
3. Check network connectivity

## Running Tests in Headless Mode

To run without opening browser windows:

1. Edit: `src\main\resources\application.properties`
2. Change:
   ```properties
   selenium.headless=true
   ```
3. Run tests:
   ```powershell
   mvn clean test
   ```

## Expected Execution Time

- **First Run**: 5-10 minutes (downloads dependencies)
- **Subsequent Runs**: 3-5 minutes (all scenarios)
- **Single Scenario**: 1-2 minutes

## Success Indicators

✅ **All tests passed:**
- Cucumber report shows green status
- Console shows "BUILD SUCCESS"
- Screenshots captured
- No errors in logs

✅ **Docker services running:**
```powershell
docker-compose ps
# All containers show "Up" status
```

✅ **Results generated:**
- `target\cucumber-reports\index.html` exists
- `target\screenshots\` folder has screenshots
- `target\logs\automation.log` exists

## Quick Command Reference

```powershell
# Start services
docker-compose up -d

# Run all tests
mvn clean test

# Run specific scenario
mvn test -Dcucumber.filter.tags="@Scenario1"

# View Selenium Grid
# Browser: http://localhost:4444/ui

# Check Docker logs
docker-compose logs -f

# Stop services
docker-compose down

# Clean everything
docker-compose down -v
mvn clean
```

## Next Steps After Running Tests

1. **Review HTML Report**: Analyze test results
2. **Check Screenshots**: Verify UI interactions
3. **Review Logs**: Check for warnings
4. **Extend Framework**: Add new test scenarios
5. **CI/CD**: Set up GitLab pipeline for automation

## Need More Help?

- **Detailed Setup**: See `SETUP_GUIDE.md`
- **Architecture**: See `ARCHITECTURE.md`
- **Framework Overview**: See `README.md`
- **Check Logs**: `target\logs\automation.log`
