# Troubleshooting Guide

## Common Issues and Solutions

This guide covers common issues you may encounter and their solutions.

---

## Setup Issues

### Prerequisites Not Installed

**Problem**: `./check-prerequisites.sh` shows missing components.

**Solution**:
```bash
# Linux/macOS - Automated installation
./setup.sh

# Or install manually
# Java: https://adoptium.net/
# Maven: https://maven.apache.org/download.cgi
# Docker: https://www.docker.com/products/docker-desktop
```

### Java Version Mismatch

**Problem**: `UnsupportedClassVersionError` or Java version too old.

**Solution**:
- Ensure Java 17 or higher is installed
- Check version: `java -version`
- Update JAVA_HOME if needed
- Remove old Java versions from PATH

### Maven Not Found

**Problem**: `mvn: command not found`

**Solution**:
```bash
# Linux
sudo apt-get install maven  # Ubuntu/Debian
sudo yum install maven     # RHEL/CentOS

# macOS
brew install maven

# Verify
mvn -version
```

---

## Docker Issues

### Docker Daemon Not Running

**Problem**: `Cannot connect to the Docker daemon`

**Solution**:
```bash
# Linux
sudo systemctl start docker
sudo systemctl enable docker

# macOS/Windows
# Start Docker Desktop application
```

### Container Name Conflict

**Problem**: `Cannot create container: name already in use`

**Solution**:
```bash
# Use cleanup script
./cleanup-containers.sh

# Or manually
docker rm -f selenium-hub selenium-node-chrome selenium-node-firefox test-automation-mysql elasticsearch kibana
```

### Port Already in Use

**Problem**: `failed to bind host port: address already in use`

**Solution**:
1. **Find what's using the port**:
   ```bash
   lsof -i :4444  # Selenium
   lsof -i :3307  # MySQL
   ```

2. **Stop the conflicting service** or change port in `docker-compose.yml`

3. **See [PORT_CONFIGURATION.md](PORT_CONFIGURATION.md)** for port details

### Container Keeps Restarting

**Problem**: Container starts then immediately stops.

**Solution**:
1. **Check logs**:
   ```bash
   docker-compose logs <service-name>
   ```

2. **Check resource limits**:
   ```bash
   docker stats
   ```

3. **Verify configuration** in `docker-compose.yml`

### Docker Permission Denied

**Problem**: `permission denied while trying to connect to the Docker daemon socket`

**Solution**:
```bash
# Add user to docker group (Linux)
sudo usermod -aG docker $USER
# Log out and log back in

# Or use sudo (not recommended)
sudo docker-compose up -d
```

---

## Service Connection Issues

### Selenium Grid Not Ready

**Problem**: `Unable to connect to Selenium Grid`

**Solution**:
1. **Check container status**:
   ```bash
   docker-compose ps selenium-hub
   ```

2. **Check logs**:
   ```bash
   docker-compose logs selenium-hub
   ```

3. **Verify grid is ready**:
   ```bash
   curl http://localhost:4444/status
   ```

4. **Wait longer** (may take 30-60 seconds to start)

### MySQL Connection Failed

**Problem**: `Communications link failure` or `Connection refused`

**Solution**:
1. **Check MySQL container**:
   ```bash
   docker-compose ps mysql
   docker-compose logs mysql
   ```

2. **Verify port** (should be 3307, not 3306):
   ```bash
   docker port test-automation-mysql
   ```

3. **Check connection string** in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3307/testautomation
   ```

4. **Wait for MySQL to initialize** (30-60 seconds after start)

### Elasticsearch Connection Failed

**Problem**: Cannot connect to Elasticsearch

**Solution**:
1. **Check if Elasticsearch is enabled**:
   ```properties
   elasticsearch.enabled=true
   ```

2. **Check container status**:
   ```bash
   docker-compose ps elasticsearch
   ```

3. **Verify connection**:
   ```bash
   curl http://localhost:9200
   ```

4. **If not needed, disable it**:
   ```properties
   elasticsearch.enabled=false
   ```

---

## Build and Test Issues

### Maven Build Fails

**Problem**: `mvn clean install` fails

**Solution**:
1. **Check Java version** (must be 17+):
   ```bash
   java -version
   ```

2. **Clean Maven cache**:
   ```bash
   rm -rf ~/.m2/repository
   mvn clean install
   ```

3. **Check network connection** (Maven needs to download dependencies)

4. **Verify pom.xml** is valid

### Tests Fail with Timeout

**Problem**: Tests timeout waiting for elements or services

**Solution**:
1. **Increase timeout** in `application.properties`:
   ```properties
   selenium.timeout=60
   ```

2. **Check Selenium Grid has available nodes**:
   - Visit http://localhost:4444/ui
   - Verify nodes are registered

3. **Check network connectivity**

### Tests Fail with Connection Errors

**Problem**: Tests can't connect to services

**Solution**:
1. **Verify all services are running**:
   ```bash
   docker-compose ps
   ```

2. **Wait for services to be ready** (30-60 seconds after start)

3. **Check service URLs** in `application.properties`

4. **Check firewall** (if applicable)

### Compilation Errors

**Problem**: `mvn compile` fails with compilation errors

**Solution**:
1. **Check Java version** (must be 17+)
2. **Clean and rebuild**:
   ```bash
   mvn clean compile
   ```
3. **Check for syntax errors** in source files
4. **Verify all dependencies** are downloaded

---

## Configuration Issues

### Properties File Not Found

**Problem**: `application.properties` not loading

**Solution**:
- Ensure file exists at: `src/main/resources/application.properties`
- Check file permissions
- Verify Spring Boot configuration

### Wrong Database Credentials

**Problem**: Database authentication fails

**Solution**:
1. **Check credentials** in `application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```

2. **Verify MySQL container environment variables** in `docker-compose.yml`

3. **Check database exists**:
   ```bash
   docker exec -it test-automation-mysql mysql -uroot -proot -e "SHOW DATABASES;"
   ```

### Port Configuration Mismatch

**Problem**: Services can't connect due to wrong ports

**Solution**:
- See [PORT_CONFIGURATION.md](PORT_CONFIGURATION.md)
- Verify `docker-compose.yml` port mappings
- Verify `application.properties` connection URLs

---

## Performance Issues

### Tests Run Slowly

**Problem**: Tests take too long to execute

**Solution**:
1. **Increase parallel threads** in `testng.xml`:
   ```xml
   thread-count="8"
   ```

2. **Use headless mode**:
   ```properties
   selenium.headless=true
   ```

3. **Check system resources**:
   ```bash
   docker stats
   ```

4. **Reduce number of browser nodes** if not needed

### Out of Memory Errors

**Problem**: `OutOfMemoryError` during test execution

**Solution**:
1. **Increase JVM memory**:
   ```bash
   export MAVEN_OPTS="-Xmx2048m"
   mvn test
   ```

2. **Reduce parallel threads** in `testng.xml`

3. **Close browsers properly** after tests

---

## Script Issues

### Permission Denied on Scripts

**Problem**: `Permission denied` when running `.sh` scripts

**Solution**:
```bash
chmod +x *.sh
```

### Script Not Found

**Problem**: `./script.sh: No such file or directory`

**Solution**:
- Ensure you're in the project root directory
- Verify script exists: `ls -la *.sh`
- Use full path if needed

### Script Fails Silently

**Problem**: Script runs but doesn't show output

**Solution**:
- Run with verbose output: `bash -x script.sh`
- Check script syntax: `bash -n script.sh`
- Verify all commands in script are available

---

## Getting Help

### Check Logs

1. **Application logs**: `target/logs/automation.log`
2. **Docker logs**: `docker-compose logs <service-name>`
3. **Maven output**: Check console output
4. **Test reports**: `target/surefire-reports/`

### Verify Setup

Run verification:
```bash
./check-prerequisites.sh
docker-compose ps
mvn clean compile
```

### Common Commands

```bash
# Check everything
./check-prerequisites.sh
docker-compose ps
docker-compose logs

# Clean up
./cleanup-containers.sh
./cleanup.sh --all

# Restart
docker-compose restart
```

---

## Still Having Issues?

1. **Check documentation**:
   - [SETUP_GUIDE.md](SETUP_GUIDE.md) - Detailed setup
   - [PORT_CONFIGURATION.md](PORT_CONFIGURATION.md) - Port configuration
   - [HOW_TO_RUN_PROJECT.md](HOW_TO_RUN_PROJECT.md) - Running tests

2. **Review logs** for specific error messages

3. **Verify your environment** matches requirements

4. **Check GitHub Issues** (if repository is public)

---

**Last Updated**: January 2025
