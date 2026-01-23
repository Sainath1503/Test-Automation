#!/bin/bash

# Cleanup Script
# This script stops Docker containers and cleans up build artifacts

set -e

echo "=========================================="
echo "Test Automation Framework - Cleanup"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if docker compose or docker-compose is available
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
elif command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    echo -e "${YELLOW}Warning: Docker Compose is not available${NC}"
    COMPOSE_CMD=""
fi

# Parse command line arguments
CLEAN_VOLUMES=false
CLEAN_BUILD=false
CLEAN_ALL=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --volumes|-v)
            CLEAN_VOLUMES=true
            shift
            ;;
        --build|-b)
            CLEAN_BUILD=true
            shift
            ;;
        --all|-a)
            CLEAN_ALL=true
            shift
            ;;
        *)
            echo -e "${YELLOW}Unknown option: $1${NC}"
            shift
            ;;
    esac
done

# Step 1: Stop Docker containers
if [ -n "$COMPOSE_CMD" ] && [ -f "docker-compose.yml" ]; then
    echo -e "${BLUE}Step 1: Stopping Docker containers...${NC}"
    
    if $CLEAN_VOLUMES || $CLEAN_ALL; then
        $COMPOSE_CMD down -v
        echo -e "${GREEN}Containers stopped and volumes removed${NC}"
    else
        $COMPOSE_CMD down
        echo -e "${GREEN}Containers stopped${NC}"
    fi
    echo ""
else
    echo -e "${YELLOW}Skipping container cleanup (docker-compose.yml not found or Docker Compose not available)${NC}"
    echo ""
fi

# Step 2: Clean Maven build artifacts
if [ "$CLEAN_BUILD" = true ] || [ "$CLEAN_ALL" = true ]; then
    echo -e "${BLUE}Step 2: Cleaning Maven build artifacts...${NC}"
    
    if command -v mvn &> /dev/null; then
        mvn clean
        echo -e "${GREEN}Maven build artifacts cleaned${NC}"
    else
        echo -e "${YELLOW}Maven not found, removing target directory manually...${NC}"
        if [ -d "target" ]; then
            rm -rf target
            echo -e "${GREEN}Target directory removed${NC}"
        fi
    fi
    echo ""
fi

# Step 3: Remove Docker images (optional, only if --all is specified)
if [ "$CLEAN_ALL" = true ]; then
    echo -e "${BLUE}Step 3: Removing Docker images...${NC}"
    read -p "Are you sure you want to remove all project Docker images? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if docker images | grep -q "selenium/hub"; then
            docker rmi selenium/hub:4.15.0 selenium/node-chrome:4.15.0 selenium/node-firefox:4.15.0 || true
        fi
        if docker images | grep -q "mysql:8.0"; then
            docker rmi mysql:8.0 || true
        fi
        if docker images | grep -q "elasticsearch"; then
            docker rmi docker.elastic.co/elasticsearch/elasticsearch:8.10.1 || true
        fi
        if docker images | grep -q "kibana"; then
            docker rmi docker.elastic.co/kibana/kibana:8.10.1 || true
        fi
        echo -e "${GREEN}Docker images removed${NC}"
    else
        echo -e "${YELLOW}Skipping image removal${NC}"
    fi
    echo ""
fi

# Summary
echo "=========================================="
echo -e "${GREEN}Cleanup completed!${NC}"
echo "=========================================="
echo ""
echo "Usage options:"
echo "  ./cleanup.sh              - Stop containers only"
echo "  ./cleanup.sh --volumes     - Stop containers and remove volumes"
echo "  ./cleanup.sh --build       - Stop containers and clean build artifacts"
echo "  ./cleanup.sh --all         - Stop containers, remove volumes, clean build, and remove images"
echo ""
