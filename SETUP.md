# Project Setup (From Scratch)

This guide sets up the **Test Automation Framework** locally and in **GitLab CI/CD**.

## Prerequisites

- **Git**
- **Java 17** (JDK 17)
- **Maven 3.9+**
- **Docker Desktop** (Windows/macOS) or Docker Engine (Linux)
  - On Windows, make sure Docker Desktop is set to **Linux containers** (WSL2 backend)
- (Optional) An IDE like IntelliJ / VS Code / Cursor

## 1) Clone the repository

```bash
git clone <your-repository-url>
cd "Test-Automation"
```

## 2) Start the required services (Selenium Grid + MySQL + optional Elasticsearch/Kibana)

From the project root:

```bash
docker-compose up -d
```

Verify containers are running:

```bash
docker-compose ps
```

Useful endpoints:

- Selenium Grid UI: `http://localhost:4444/ui`
- Selenium status: `http://localhost:4444/status`
- MySQL: `localhost:3306`
- Elasticsearch: `http://localhost:9200` (optional)
- Kibana: `http://localhost:5933` (optional; project maps 5601 → 5933)

## 3) (Optional) Verify / adjust config

The default config is in:

- `src/main/resources/application.properties`

Key properties:

- `selenium.grid.url` (default: `http://localhost:4444/wd/hub`)
- `spring.datasource.url` (default: `jdbc:mysql://localhost:3306/testautomation?createDatabaseIfNotExist=true`)
- `spring.datasource.username` / `spring.datasource.password` (default: `root` / `root`)
- `elasticsearch.enabled` (default: `false`)

## 3.1) Elasticsearch + Kibana (optional)

### Enable/disable Elasticsearch in the framework

In `src/main/resources/application.properties`:

```properties
elasticsearch.enabled=true
elasticsearch.host=localhost
elasticsearch.port=9200
```

If you don’t want Elasticsearch, keep:

```properties
elasticsearch.enabled=false
```

### Verify Elasticsearch is running (local)

After `docker-compose up -d`, run:

```bash
docker-compose ps elasticsearch kibana
```

Then verify Elasticsearch health:

```bash
curl http://localhost:9200
curl http://localhost:9200/_cluster/health?pretty
```

Kibana UI (project mapping): `http://localhost:5933`

### Enable/disable Elasticsearch in GitLab CI

Your pipeline supports these env vars (GitLab → Settings → CI/CD → Variables):

- `ELASTICSEARCH_ENABLED` = `true` or `false`
- `ELASTICSEARCH_HOST` = `elasticsearch` (CI) or `localhost` (local)
- `ELASTICSEARCH_PORT` = `9200`

## 4) Build and run tests locally

### Run all tests

```bash
mvn clean test
```

### Run a specific scenario/tag (example)

```bash
mvn test -Dcucumber.filter.tags="@Scenario1"
```

### Outputs (after running tests)

- Screenshots: `target/screenshots/`
- Logs: `target/logs/`
- Cucumber outputs: `target/cucumber*` (HTML/JSON/JUnit depending on configuration)
- TestNG reports: `target/surefire-reports/`

## 5) GitLab CI/CD setup (Runner + Pipeline)

Your pipeline is defined in:

- `.gitlab-ci.yml`

It uses Docker-in-Docker to start services with `docker-compose`, then runs Maven tests inside a Maven container.

### 5.1 Push the code to GitLab

```bash
git remote add origin <gitlab_repo_url>
git push -u origin main
```

### 5.2 Create a GitLab Runner (recommended: self-hosted)

In GitLab:

- Project → **Settings** → **CI/CD** → **Runners**
- Create a **Project Runner** and copy the **runner token**

### 5.3 Install GitLab Runner (Windows)

Run PowerShell:

```powershell
mkdir C:\GitLab-Runner
cd C:\GitLab-Runner

Invoke-WebRequest -Uri "https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-windows-amd64.exe" -OutFile "gitlab-runner.exe"
.\gitlab-runner.exe --version
```

Register the runner (use the token from GitLab):

```powershell
cd C:\GitLab-Runner
.\gitlab-runner.exe register --url https://gitlab.com --token <YOUR_NEW_TOKEN>
```

During prompts choose:

- **executor**: `docker`
- **default image**: `docker:24.0.5`
- **tags**: leave empty (or set tags and use the same tags in `.gitlab-ci.yml`)

> Security note: never paste runner tokens in chat. If a token is exposed, reset it in GitLab.

### 5.4 Enable privileged mode (required for docker:dind)

Edit:

- `C:\GitLab-Runner\config.toml`

Ensure:

```toml
[runners.docker]
  privileged = true
```

### 5.5 Install and start runner as a Windows service (Admin PowerShell)

Open **PowerShell as Administrator**, then:

```powershell
cd C:\GitLab-Runner
.\gitlab-runner.exe install
.\gitlab-runner.exe start
.\gitlab-runner.exe status
```

### 5.6 Ensure your project uses the correct runner

If your jobs are running on GitLab shared runners instead of your runner:

- Option A: **Disable shared runners** for the project
- Option B: Add a **tag** to your runner (example: `automention`) and add the same to jobs in `.gitlab-ci.yml`:

```yaml
tags: ["automention"]
```

Also ensure in GitLab Runner settings:

- **Run untagged jobs** is ON (only needed if you are not using tags)

### 5.7 Trigger a pipeline test

- GitLab → Build → Pipelines → **Run pipeline**
  - or push a small commit:

```bash
git commit --allow-empty -m "ci: trigger pipeline"
git push
```

## 6) Common CI issues and fixes

### “Unsupported config option for services / volumes” in `docker-compose.yml`

Cause: CI environment treated the compose file as old format.

Fix: Ensure `docker-compose.yml` has:

```yaml
version: "3.8"
```

### “openjdk17 (missing) required by world[openjdk17]”

Cause: Installing Java via `apk add openjdk17` fails in some Alpine-based images.

Fix: Run Maven tests in a Maven container image (`maven:3.9.5-eclipse-temurin-17`) instead of installing Java/Maven inside the compose image.

### “Cannot connect to the Docker daemon” / DinD failures

Fix checklist:

- Runner uses **docker executor**
- Runner has **privileged = true**
- Job variables include `DOCKER_HOST=tcp://docker:2375` and `DOCKER_TLS_CERTDIR=""`

### Selenium not ready / flaky startup

Fix: increase the wait time before tests:

- change `sleep 30` → `sleep 60` (or add a health check loop to wait for `http://selenium-hub:4444/status`)

## 7) Useful commands

Stop and remove all containers + volumes:

```bash
docker-compose down -v
```

View logs:

```bash
docker-compose logs -f selenium-hub
docker-compose logs -f mysql
```

