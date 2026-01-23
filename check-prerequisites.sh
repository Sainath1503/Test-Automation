#!/bin/bash

# Check Prerequisites Script
# This script verifies if all required tools are installed

set -e

echo "=========================================="
echo "Checking Prerequisites for Test Automation Framework"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Track missing prerequisites
MISSING_PREREQS=0

# Function to check if command exists
check_command() {
    if command -v "$1" &> /dev/null; then
        echo -e "${GREEN}✓${NC} $1 is installed"
        if [ "$1" = "java" ]; then
            VERSION=$(java -version 2>&1 | head -n 1)
            echo "  Version: $VERSION"
        elif [ "$1" = "mvn" ]; then
            VERSION=$(mvn -version | head -n 1)
            echo "  Version: $VERSION"
        elif [ "$1" = "docker" ]; then
            VERSION=$(docker --version)
            echo "  Version: $VERSION"
        elif [ "$1" = "docker-compose" ] || [ "$1" = "docker" ] && [ "$2" = "compose" ]; then
            if docker compose version &> /dev/null; then
                VERSION=$(docker compose version)
                echo "  Version: $VERSION"
            else
                echo -e "${RED}✗${NC} docker compose is not available"
                MISSING_PREREQS=$((MISSING_PREREQS + 1))
            fi
        fi
        return 0
    else
        echo -e "${RED}✗${NC} $1 is NOT installed"
        MISSING_PREREQS=$((MISSING_PREREQS + 1))
        return 1
    fi
}

# Check Java
echo "Checking Java..."
if check_command "java"; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${YELLOW}⚠${NC} Java version is less than 17. Java 17+ is required."
        MISSING_PREREQS=$((MISSING_PREREQS + 1))
    fi
else
    echo "  Java 17 or higher is required"
fi
echo ""

# Check Maven
echo "Checking Maven..."
if check_command "mvn"; then
    MVN_VERSION=$(mvn -version | head -n 1 | awk '{print $3}')
    MVN_MAJOR=$(echo $MVN_VERSION | cut -d'.' -f1)
    MVN_MINOR=$(echo $MVN_VERSION | cut -d'.' -f2)
    if [ "$MVN_MAJOR" -lt 3 ] || ([ "$MVN_MAJOR" -eq 3 ] && [ "$MVN_MINOR" -lt 6 ]); then
        echo -e "${YELLOW}⚠${NC} Maven version is less than 3.6. Maven 3.6+ is required."
        MISSING_PREREQS=$((MISSING_PREREQS + 1))
    fi
else
    echo "  Maven 3.6+ is required"
fi
echo ""

# Check Docker
echo "Checking Docker..."
if check_command "docker"; then
    # Check if Docker daemon is running
    if ! docker info &> /dev/null; then
        echo -e "${YELLOW}⚠${NC} Docker is installed but daemon is not running"
        echo "  Please start Docker Desktop or Docker daemon"
        MISSING_PREREQS=$((MISSING_PREREQS + 1))
    fi
else
    echo "  Docker is required"
fi
echo ""

# Check Docker Compose
echo "Checking Docker Compose..."
if docker compose version &> /dev/null; then
    echo -e "${GREEN}✓${NC} docker compose is available"
    VERSION=$(docker compose version)
    echo "  Version: $VERSION"
elif command -v docker-compose &> /dev/null; then
    echo -e "${GREEN}✓${NC} docker-compose is available"
    VERSION=$(docker-compose --version)
    echo "  Version: $VERSION"
else
    echo -e "${RED}✗${NC} Docker Compose is NOT available"
    echo "  Docker Compose is required"
    MISSING_PREREQS=$((MISSING_PREREQS + 1))
fi
echo ""

# Check Git (optional but recommended)
echo "Checking Git..."
if check_command "git"; then
    echo "  Git is available (optional but recommended)"
else
    echo "  Git is not installed (optional)"
fi
echo ""

# Summary
echo "=========================================="
if [ $MISSING_PREREQS -eq 0 ]; then
    echo -e "${GREEN}All prerequisites are met!${NC}"
    echo "You can proceed with the setup."
    exit 0
else
    echo -e "${RED}Missing prerequisites: $MISSING_PREREQS${NC}"
    echo ""
    echo "Please install the missing prerequisites before proceeding."
    echo "Run './setup.sh' to attempt automatic installation (Linux/macOS only)."
    exit 1
fi
