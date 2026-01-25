# Docker Setup

## Docker Architecture

The Test Automation Framework uses Docker Compose to manage multiple services:

- **Selenium Grid Hub** - Central hub for browser automation
- **Selenium Chrome Node** - Chrome browser instances
- **Selenium Firefox Node** - Firefox browser instances
- **MySQL** - Database for test data
- **Elasticsearch** (optional) - Search and analytics
- **Kibana** (optional) - Visualization for Elasticsearch

---

## Service Configuration

### docker-compose.yml

All services are defined in `docker-compose.yml`. Key configurations:

#### Selenium Grid

```yaml
selenium-hub:
  image: selenium/hub:4.15.0
  ports:
    - "4444:4444"
  environment:
    - GRID_MAX_SESSION=16
```

#### MySQL

```yaml
mysql:
  image: mysql:8.0
  container_name: test-automation-mysql
  ports:
    - "3307:3306"  # Using 3307 to avoid conflict
  environment:
    - MYSQL_ROOT_PASSWORD=root
    - MYSQL_DATABASE=testautomation
```

#### Elasticsearch (Optional)

```yaml
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.10.1
  ports:
    - "9200:9200"
  environment:
    - discovery.type=single-node
    - xpack.security.enabled=false
```

---

## Container Management

### Start All Services

```bash
docker-compose up -d
```

### Stop All Services

```bash
docker-compose down
```

### Stop and Remove Volumes

```bash
docker-compose down -v
```

### View Container Status

```bash
docker-compose ps
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f selenium-hub
docker-compose logs -f mysql
```

### Restart Services

```bash
docker-compose restart
```

### Rebuild Containers

```bash
docker-compose up -d --build
```

---

## Resource Allocation

### Default Resource Usage

- **Selenium Hub**: ~200MB RAM
- **Chrome Node**: ~500MB RAM per instance
- **Firefox Node**: ~400MB RAM per instance
- **MySQL**: ~300MB RAM
- **Elasticsearch**: ~512MB RAM (if enabled)
- **Kibana**: ~200MB RAM (if enabled)

**Total**: ~2-3GB RAM (without Elasticsearch), ~3-4GB RAM (with Elasticsearch)

### Adjusting Resources

Edit `docker-compose.yml` to adjust:

```yaml
services:
  selenium-node-chrome:
    shm_size: 2gb  # Shared memory size
    deploy:
      resources:
        limits:
          memory: 1G
        reservations:
          memory: 512M
```

---

## Security Considerations

### Network Isolation

Containers run in an isolated Docker network. External access is only through exposed ports.

### Credentials

Default credentials are set in `docker-compose.yml`:
- MySQL root password: `root`
- MySQL user: `testuser`
- MySQL password: `testpass`

**For production**, change these credentials!

### Volume Mounts

Only necessary volumes are mounted:
- MySQL data: `mysql-data` volume
- Database init script: `./src/main/resources/database/init.sql`
- Elasticsearch data: `es-data` volume

---

## Troubleshooting Docker Issues

### Container Won't Start

1. **Check logs**:
   ```bash
   docker-compose logs <service-name>
   ```

2. **Check port conflicts**:
   ```bash
   lsof -i :4444  # Selenium
   lsof -i :3307  # MySQL
   ```

3. **Remove and recreate**:
   ```bash
   ./cleanup-containers.sh
   docker-compose up -d
   ```

### Container Keeps Restarting

1. **Check container logs**:
   ```bash
   docker logs <container-name>
   ```

2. **Check resource limits**:
   ```bash
   docker stats
   ```

3. **Verify configuration** in `docker-compose.yml`

### Port Already in Use

**Port Configuration:**
- **Selenium Hub**: 4444
- **MySQL**: 3307 (using 3307 to avoid conflict with local MySQL on 3306)
- **Elasticsearch**: 9200
- **Kibana**: 5933 (using 5933 instead of 5601 to avoid Windows port conflicts)

**To resolve port conflicts:**
1. Find what's using the port: `lsof -i :PORT_NUMBER` (Linux/macOS) or `netstat -ano | findstr :PORT_NUMBER` (Windows)
2. Stop the conflicting service or change the port in `docker-compose.yml`
3. Update `application.properties` if you change MySQL port

### Docker Daemon Not Running

```bash
# Linux
sudo systemctl start docker

# macOS/Windows
# Start Docker Desktop application
```

### Permission Denied

```bash
# Add user to docker group (Linux)
sudo usermod -aG docker $USER
# Log out and log back in
```

---

## Dockerfile

**Note**: The `Dockerfile` is present but **not required** for normal usage. It's used for building the application container, but tests typically run on the host machine connecting to Docker services.

The Dockerfile is used for:
- CI/CD pipelines
- Containerized test execution
- Production deployments

For local development, only `docker-compose.yml` is needed.

---

## Best Practices

1. **Always use `docker-compose down`** before stopping to clean up properly
2. **Use volumes** for persistent data (MySQL, Elasticsearch)
3. **Check logs** when services fail to start
4. **Monitor resource usage** with `docker stats`
5. **Keep images updated** by pulling latest versions

---

## Service Health Checks

### Selenium Grid

```bash
curl http://localhost:4444/status
```

Should return JSON with grid status.

### MySQL

```bash
docker exec test-automation-mysql mysqladmin ping -h localhost
```

Should return "mysqld is alive".

### Elasticsearch

```bash
curl http://localhost:9200
```

Should return cluster information.

---

## Next Steps

- **Troubleshoot issues**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
- **Run tests**: [HOW_TO_RUN_PROJECT.md](HOW_TO_RUN_PROJECT.md)

---

**Last Updated**: January 2025
