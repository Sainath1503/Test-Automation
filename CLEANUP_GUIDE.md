# Cleanup Guide

## Files to Remove

Before pushing to GitHub or sharing the project, clean up generated files and artifacts.

---

## What to Clean Up

### Build Artifacts

- `target/` directory - Contains compiled classes, test reports, screenshots
- `.m2/repository/` - Maven local repository cache (if in project)

### IDE Files

- `.idea/` - IntelliJ IDEA configuration
- `*.iml` - IntelliJ module files
- `.vscode/` - VS Code configuration
- `*.swp`, `*.swo` - Vim swap files

### OS Files

- `.DS_Store` - macOS Finder metadata
- `Thumbs.db` - Windows thumbnail cache

### Logs

- `*.log` files
- `logs/` directory

### Docker

- `.dockerignore` (if not needed)

---

## Cleanup Script Usage

### Basic Cleanup

Stop containers and clean build artifacts:

```bash
./cleanup.sh --build
```

### Full Cleanup

Stop containers, remove volumes, clean build, and remove images:

```bash
./cleanup.sh --all
```

### Container Cleanup

Forcefully remove all project containers:

```bash
./cleanup-containers.sh
```

### Cleanup Options

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

---

## Pre-Push Checklist

Before pushing to GitHub:

- [ ] Run `./cleanup.sh --build` to remove build artifacts
- [ ] Verify `.gitignore` excludes unwanted files
- [ ] Check for sensitive data (passwords, API keys)
- [ ] Remove IDE-specific files
- [ ] Remove OS-specific files
- [ ] Review what will be committed: `git status`
- [ ] Test that project still builds after cleanup

---

## File Size Reduction

### Large Files to Exclude

1. **target/ directory** (~100MB+)
   - Compiled classes
   - Test reports
   - Screenshots
   - Logs

2. **Maven repository cache** (if in project)
   - `.m2/repository/` (~500MB+)

3. **Docker images** (don't commit)
   - Already pulled from Docker Hub

### .gitignore Contents

Ensure `.gitignore` includes:

```gitignore
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
.m2/repository/

# IDE
.idea/
*.iml
.vscode/
*.swp
*.swo
*~

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/

# Docker
.dockerignore
```

---

## Manual Cleanup

### Remove Build Artifacts

```bash
# Remove target directory
rm -rf target/

# Or use Maven
mvn clean
```

### Remove IDE Files

```bash
# IntelliJ
rm -rf .idea/
rm -f *.iml

# VS Code
rm -rf .vscode/

# Vim
rm -f *.swp *.swo
```

### Remove OS Files

```bash
# macOS
find . -name .DS_Store -delete

# Windows
del /s Thumbs.db
```

### Remove Logs

```bash
find . -name "*.log" -delete
rm -rf logs/
```

---

## Docker Cleanup

### Stop and Remove Containers

```bash
docker-compose down
```

### Remove Volumes

```bash
docker-compose down -v
```

### Remove Images

```bash
# Remove project images
docker rmi selenium/hub:4.15.0
docker rmi selenium/node-chrome:4.15.0
docker rmi selenium/node-firefox:4.15.0
docker rmi mysql:8.0
docker rmi docker.elastic.co/elasticsearch/elasticsearch:8.10.1
docker rmi docker.elastic.co/kibana/kibana:8.10.1

# Or use cleanup script
./cleanup.sh --all
```

### Clean Docker System

```bash
# Remove unused containers, networks, images
docker system prune

# Remove everything (use with caution!)
docker system prune -a --volumes
```

---

## What to Keep

**Keep these files**:

- Source code (`src/`)
- Configuration files (`pom.xml`, `docker-compose.yml`, etc.)
- Documentation (`.md` files)
- Scripts (`.sh` files)
- `.gitignore`
- `README.md`
- `testng.xml`

---

## Automated Cleanup Script

Create a comprehensive cleanup script:

```bash
#!/bin/bash
# cleanup-all.sh

echo "Cleaning up project..."

# Stop containers
docker-compose down 2>/dev/null || true

# Remove build artifacts
mvn clean 2>/dev/null || rm -rf target/

# Remove IDE files
rm -rf .idea/ .vscode/ *.iml 2>/dev/null || true

# Remove OS files
find . -name .DS_Store -delete 2>/dev/null || true
find . -name Thumbs.db -delete 2>/dev/null || true

# Remove logs
find . -name "*.log" -delete 2>/dev/null || true
rm -rf logs/ 2>/dev/null || true

echo "Cleanup complete!"
```

---

## Verification

After cleanup, verify:

1. **Project still builds**:
   ```bash
   mvn clean compile
   ```

2. **No sensitive data**:
   ```bash
   git status
   git diff
   ```

3. **File sizes are reasonable**:
   ```bash
   du -sh .
   ```

---

## Best Practices

1. **Clean before committing**: Always run cleanup before git commit
2. **Use .gitignore**: Ensure unwanted files are excluded
3. **Regular cleanup**: Clean up regularly, not just before push
4. **Keep scripts**: Don't delete cleanup scripts
5. **Document cleanup**: Document what gets cleaned

---

## Quick Cleanup Commands

```bash
# Quick cleanup (containers + build)
./cleanup.sh --build

# Full cleanup
./cleanup.sh --all

# Container cleanup only
./cleanup-containers.sh

# Maven clean
mvn clean
```

---

## Next Steps

After cleanup:

1. **Verify project builds**: `mvn clean compile`
2. **Review changes**: `git status`
3. **Commit**: `git commit -m "Clean up build artifacts"`
4. **Push**: `git push`

---

**Last Updated**: January 2025
