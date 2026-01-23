#!/bin/bash

# Run Project Script
# This script starts Docker containers, builds the project, and runs tests

set -e

echo "=========================================="
echo "Test Automation Framework - Run Project"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo -e "${RED}Error: Docker daemon is not running${NC}"
    echo "Please start Docker Desktop or Docker daemon and try again."
    exit 1
fi

# Function to check if port is in use
check_port() {
    local PORT=$1
    local SERVICE=$2
    if command -v lsof &> /dev/null; then
        if lsof -i :$PORT &> /dev/null; then
            return 0  # Port is in use
        fi
    elif command -v netstat &> /dev/null; then
        if netstat -tuln 2>/dev/null | grep -q ":$PORT "; then
            return 0  # Port is in use
        fi
    elif command -v ss &> /dev/null; then
        if ss -tuln 2>/dev/null | grep -q ":$PORT "; then
            return 0  # Port is in use
        fi
    fi
    return 1  # Port is not in use
}

# Check for port conflicts
echo -e "${BLUE}Checking for port conflicts...${NC}"
PORTS_TO_CHECK=("4444:Selenium Hub" "3307:MySQL" "9200:Elasticsearch" "5933:Kibana")
PORT_CONFLICTS=0

for port_info in "${PORTS_TO_CHECK[@]}"; do
    IFS=':' read -r port service <<< "$port_info"
    if check_port "$port"; then
        echo -e "${YELLOW}Warning: Port $port ($service) is already in use${NC}"
        PORT_CONFLICTS=$((PORT_CONFLICTS + 1))
    fi
done

if [ $PORT_CONFLICTS -gt 0 ]; then
    echo -e "${YELLOW}Some ports are in use. The script will attempt to use docker-compose which may handle this.${NC}"
    echo ""
fi

# Check if docker-compose.yml exists
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}Error: docker-compose.yml not found${NC}"
    echo "Please run this script from the project root directory."
    exit 1
fi

# Function to check if docker compose or docker-compose is available
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
elif command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    echo -e "${RED}Error: Docker Compose is not available${NC}"
    exit 1
fi

# Function to wait for service to be ready
wait_for_service() {
    local SERVICE_URL=$1
    local SERVICE_NAME=$2
    local MAX_ATTEMPTS=30
    local ATTEMPT=0
    
    echo -e "${YELLOW}Waiting for $SERVICE_NAME to be ready...${NC}"
    while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
        if curl -s "$SERVICE_URL" &> /dev/null; then
            echo -e "${GREEN}$SERVICE_NAME is ready!${NC}"
            return 0
        fi
        ATTEMPT=$((ATTEMPT + 1))
        echo -n "."
        sleep 2
    done
    echo ""
    echo -e "${YELLOW}Warning: $SERVICE_NAME may not be fully ready, but continuing...${NC}"
    return 1
}

# Function to wait for MySQL
wait_for_mysql() {
    local MAX_ATTEMPTS=30
    local ATTEMPT=0
    
    echo -e "${YELLOW}Waiting for MySQL to be ready...${NC}"
    while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
        if docker exec test-automation-mysql mysqladmin ping -h localhost --silent &> /dev/null; then
            echo -e "${GREEN}MySQL is ready!${NC}"
            return 0
        fi
        ATTEMPT=$((ATTEMPT + 1))
        echo -n "."
        sleep 2
    done
    echo ""
    echo -e "${YELLOW}Warning: MySQL may not be fully ready, but continuing...${NC}"
    return 1
}

# Step 1: Stop and remove existing containers (if any)
echo -e "${BLUE}Step 1: Cleaning up existing containers...${NC}"
$COMPOSE_CMD down 2>/dev/null || true

# Remove any orphaned containers with the same names
echo -e "${YELLOW}Removing any orphaned containers...${NC}"
docker rm -f selenium-hub selenium-node-chrome selenium-node-firefox test-automation-mysql elasticsearch kibana 2>/dev/null || true

# Step 2: Start Docker containers
echo -e "${BLUE}Step 2: Starting Docker containers...${NC}"
$COMPOSE_CMD up -d

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Failed to start Docker containers${NC}"
    echo -e "${YELLOW}Trying to remove conflicting containers and retrying...${NC}"
    docker rm -f selenium-hub selenium-node-chrome selenium-node-firefox test-automation-mysql elasticsearch kibana 2>/dev/null || true
    sleep 2
    $COMPOSE_CMD up -d
    if [ $? -ne 0 ]; then
        echo -e "${RED}Error: Failed to start Docker containers after cleanup${NC}"
        exit 1
    fi
fi

echo -e "${GREEN}Docker containers started${NC}"
echo ""

# Step 3: Wait for services to be ready
echo -e "${BLUE}Step 3: Waiting for services to be ready...${NC}"
sleep 5

# Wait for Selenium Hub
wait_for_service "http://localhost:4444/status" "Selenium Hub" || true

# Wait for MySQL
wait_for_mysql || true

# Wait for Elasticsearch (optional)
if $COMPOSE_CMD ps elasticsearch | grep -q "Up"; then
    wait_for_service "http://localhost:9200" "Elasticsearch" || true
fi

echo ""

# Step 4: Verify containers are running
echo -e "${BLUE}Step 4: Verifying containers...${NC}"
$COMPOSE_CMD ps
echo ""

# Step 5: Build the project
echo -e "${BLUE}Step 5: Building the project...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven is not installed${NC}"
    echo "Please install Maven or use Docker to build the project."
    exit 1
fi

mvn clean compile -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Build failed${NC}"
    echo "Please check the error messages above."
    exit 1
fi

echo -e "${GREEN}Build completed successfully${NC}"
echo ""

# Step 6: Run tests
echo -e "${BLUE}Step 6: Running tests...${NC}"
echo ""

# Check if a specific tag was provided
if [ -n "$1" ]; then
    echo -e "${YELLOW}Running tests with tag: $1${NC}"
    mvn test -Dcucumber.filter.tags="$1"
else
    echo -e "${YELLOW}Running all tests...${NC}"
    mvn test
fi

TEST_EXIT_CODE=$?

echo ""
echo "=========================================="
if [ $TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}Tests completed successfully!${NC}"
else
    echo -e "${YELLOW}Tests completed with some failures${NC}"
fi
echo "=========================================="
echo ""

# Display report locations
echo -e "${BLUE}Test Reports:${NC}"
echo "  - Cucumber HTML Report: target/cucumber-report.html"
echo "  - TestNG Reports: target/surefire-reports/index.html"
echo "  - Screenshots: target/screenshots/"
echo "  - Logs: target/logs/"
echo ""

# Display service URLs
echo -e "${BLUE}Service URLs:${NC}"
echo "  - Selenium Grid UI: http://localhost:4444/ui"
echo "  - Selenium Status: http://localhost:4444/status"
echo "  - MySQL: localhost:3307 (using 3307 to avoid conflict with local MySQL on 3306)"
if $COMPOSE_CMD ps elasticsearch | grep -q "Up"; then
    echo "  - Elasticsearch: http://localhost:9200"
    echo "  - Kibana: http://localhost:5933"
fi
echo ""

# Note about keeping containers running
echo -e "${YELLOW}Note: Docker containers are still running.${NC}"
echo "To stop them, run: ./cleanup.sh"
echo ""

exit $TEST_EXIT_CODE
